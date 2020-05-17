package stfXCore.Services.Frames;

import lombok.Data;
import stfXCore.Models.Storyboard.State;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;

import java.util.ArrayList;
import java.util.List;

@Data
public class Frame {

    private static final int LOWER_BOUND_INDEX = 0;
    private static final int UPPER_BOUND_INDEX = 1;

    private ArrayList<EventDataWithTrigger> events;

    // Saving redudant data and as an array, for json
    private ArrayList<Long> temporalRange;

    private List<State> phenomena;

    public Frame(List<State> phenomena) {
        this.phenomena = phenomena;
        this.temporalRange = new ArrayList<>();
        temporalRange.add(this.phenomena.get(0).getTimestamp());
        temporalRange.add(this.phenomena.get(phenomena.size() - 1).getTimestamp());
        this.events = new ArrayList<>();
    }

    public void addEvent(EventDataWithTrigger event) {
        events.add(event);
    }

    public Long lowerBound() {
        return this.temporalRange.get(LOWER_BOUND_INDEX);
    }

    public Long upperBound() {
        return this.temporalRange.get(UPPER_BOUND_INDEX);
    }

    private EventDataWithTrigger getSimilarEvent(EventDataWithTrigger similarEvent) {
        Event.ThresholdTrigger threshold = similarEvent.getThreshold();
        Event.Transformation type = similarEvent.getType();

        for (EventDataWithTrigger event : events) {
            if (event.getThreshold() == threshold &&
                    event.getType() == type &&
                    !(event.getThreshold() == Event.ThresholdTrigger.DIRECTED_ACC &&
                            event.getTrigger().changeDirection(similarEvent.getTrigger())))
                return event;
        }
        return null;
    }

    public Frame joinFrames(Frame frame) {
        ArrayList<EventDataWithTrigger> frameEvents = frame.getEvents();
        // If size different we know for sure events can not be joined
        if (events.size() != frameEvents.size())
            return null;

        ArrayList<EventDataWithTrigger> jointEvents = new ArrayList<>();
        for (EventDataWithTrigger event : frameEvents) {
            EventDataWithTrigger similarEvent = getSimilarEvent(event);
            if (similarEvent == null) {
                return null;
            } else {
                jointEvents.add(event.joinEvents(similarEvent));
            }
        }
        // Merging phenomena
        List<State> jointPhenomena = new ArrayList<>(this.phenomena);
        jointPhenomena.remove(this.phenomena.size() - 1); // Removing last element common to both lists
        jointPhenomena.addAll(frame.phenomena);

        Frame jointFrame = new Frame(jointPhenomena);
        for (EventDataWithTrigger event : jointEvents)
            jointFrame.addEvent(event);
        return jointFrame;
    }
}

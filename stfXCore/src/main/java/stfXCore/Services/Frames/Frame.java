package stfXCore.Services.Frames;

import lombok.Data;
import stfXCore.Models.Storyboard.State;
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
}

package stfXCore.Services.TemporalFrames;

import lombok.Data;
import stfXCore.Models.Storyboard.State;
import stfXCore.Services.Events.EventDataWithTrigger;

import java.util.ArrayList;

@Data
public class Frame {

    private static final int UPPER_BOUND_INDEX = 2;

    private ArrayList<State> phenomena;

    private ArrayList<EventDataWithTrigger> events;

    // Saving redudant data and as an array, for json
    private ArrayList<Float> temporalRange;

    Frame(ArrayList<State> phenomena)  {
        this.phenomena = phenomena;
        this.temporalRange = new ArrayList<>();
        temporalRange.add(this.phenomena.get(0).getTimestamp());
        temporalRange.add(this.phenomena.get(phenomena.size() - 1).getTimestamp());
        this.events = new ArrayList<>();
    }

    Frame addEvent(EventDataWithTrigger event) {
        events.add(event);
        return this;
    }

    public Float getUpperBound() {
        return this.temporalRange.get(UPPER_BOUND_INDEX);
    }
}

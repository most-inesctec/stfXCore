package stfXCore.Services.TemporalFrames;

import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;
import stfXCore.Models.Storyboard.State;
import stfXCore.Services.Events.EventDataWithTrigger;

import java.util.ArrayList;
import java.util.List;

@Data
public class Frame {

    private ArrayList<EventDataWithTrigger> events;

    // Saving redudant data and as an array, for json
    private ArrayList<Float> temporalRange;

    private List<State> phenomena;

    Frame(List<State> phenomena)  {
        this.phenomena = phenomena;
        this.temporalRange = new ArrayList<>();
        temporalRange.add(this.phenomena.get(0).getTimestamp());
        temporalRange.add(this.phenomena.get(phenomena.size() - 1).getTimestamp());
        this.events = new ArrayList<>();
    }

    void addEvent(EventDataWithTrigger event) {
        events.add(event);
    }

    public Float upperBound() {
        return this.temporalRange.get(1);
    }
}

package stfXCore.Services.Events;

import lombok.Data;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.State;

import java.util.ArrayList;

@Data
public class Event {

    public enum ThresholdTrigger {
        DELTA,
        DIRECTED_ACC,
        ABSOLUTE_ACC
    }

    public enum Transformation {
        TRANSLATION,
        ROTATION,
        UNIFORM_SCALE
    }

    private ThresholdTrigger trigger;

    private Transformation type;

    private float triggerValue;

    private ArrayList<State> phenomena;

    Event(ThresholdTrigger trigger, Transformation type, float triggerValue) {
        this.trigger = trigger;
        this.type = type;
        this.triggerValue = triggerValue;
    }

    public ArrayList<EventWrapper> getWrappers() {
        return new EventWrapper[]{
                new StartEventWrapper(this).setTimestamp(
                        this.phenomena.get(0).getTimestamp()),
                new EndEventWrapper(this).setTimestamp(
                        this.phenomena.get(this.phenomena.size() - 1).getTimestamp())
        };
    }
}

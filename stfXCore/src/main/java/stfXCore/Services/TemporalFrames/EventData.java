package stfXCore.Services.TemporalFrames;

import stfXCore.Services.Events.Event;

public class EventData {

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

    private Event.ThresholdTrigger trigger;

    private Event.Transformation type;

    private float triggerValue;
}

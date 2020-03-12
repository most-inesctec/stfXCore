package stfXCore.Services;

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

    Event(ThresholdTrigger trigger, Transformation type, float triggerValue) {
        this.trigger = trigger;
        this.type = type;
        this.triggerValue = triggerValue;
    }
}

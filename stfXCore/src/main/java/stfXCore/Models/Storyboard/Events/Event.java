package stfXCore.Models.Storyboard.Events;

import lombok.Data;
import stfXCore.Utils.Pair;

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

    private EventData data;

    /**
     * First Element are timestamps
     * Second Element are triggerValues
     */
    private ArrayList<Pair<Float, Float>> values;

    public Event(ThresholdTrigger trigger, Transformation type) {
        this.data = new EventData(trigger, type);
    }

    public Event setValues(ArrayList<Pair<Float, Float>> values) {
        this.values = values;
        return this;
    }

    public ArrayList<EventWrapper> getWrappers() {
        ArrayList<EventWrapper> wrappers = new ArrayList<>();
        wrappers.add(
                new StartEventWrapper(this).setTimestamp(
                        values.get(0).getFirst()));
        wrappers.add(
                new EndEventWrapper(this).setTimestamp(
                        values.get(values.size() - 1).getFirst()));
        return wrappers;
    }

    /**
     * Get the triggerValue associated to the given timestamp
     */
    public Float getTriggerValue(Float fromTimestamp, Float toTimestamp) {
        Float fromValue = 0f, toValue = 0f;

        for (Pair<Float, Float> pair : values) {
            if (pair.getFirst().equals(fromTimestamp))
                fromValue = pair.getSecond();
            else if (pair.getFirst().equals(toTimestamp))
                toValue = pair.getSecond();
        }
        return toValue - fromValue;
    }
}

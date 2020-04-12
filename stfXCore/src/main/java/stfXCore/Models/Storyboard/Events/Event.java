package stfXCore.Models.Storyboard.Events;

import lombok.Data;
import stfXCore.Services.DataTypes.TransformationDataType;
import stfXCore.Utils.Pair;

import java.util.ArrayList;

@Data
public class Event<T extends TransformationDataType> {

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
    private ArrayList<Pair<Float, T>> values;

    public Event(ThresholdTrigger trigger, Transformation type) {
        this.data = new EventData(trigger, type);
    }

    public Event<T> setValues(ArrayList<Pair<Float, T>> values) {
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
    public T getTriggerValue(Float fromTimestamp, Float toTimestamp) {
        T fromValue = null;
        T toValue = null;

        for (Pair<Float, T> pair : values) {
            if (pair.getFirst().equals(fromTimestamp))
                fromValue = pair.getSecond();
            else if (pair.getFirst().equals(toTimestamp))
                toValue = pair.getSecond();
        }

        return (T) toValue.subtract(fromValue.getTransformation());
    }
}

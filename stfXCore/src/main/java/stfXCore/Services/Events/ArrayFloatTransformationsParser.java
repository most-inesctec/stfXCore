package stfXCore.Services.Events;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ArrayFloatTransformationsParser extends TransformationsParser<ArrayList<Float>> {

    private static final Float DIRECTION_THRESHOLD = (float) Math.PI / 4;

    @Override
    protected boolean verifyNull(ArrayList<Float> values) {
        for (Float value : values)
            if (value != 0)
                return false;
        return true;
    }

    @Override
    protected ArrayList<Float> getNullValue() {
        return new ArrayList<>(Arrays.asList(0f, 0f));
    }

    @Override
    protected Float getValue(ArrayList<Float> values) {
        return (float) Math.sqrt(values.stream().reduce(
                0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
    }

    @Override
    protected boolean changeDirection(ArrayList<Float> values, ArrayList<Float> previousValues) {
        Float dotProduct = 0f;

        for (int i = 0; i < values.size(); i++)
            dotProduct += values.get(i) * previousValues.get(i);

        // If angle between two vectors > 45º say direction changed
        return Math.acos(dotProduct / (getValue(values) * getValue(previousValues))) > DIRECTION_THRESHOLD;
    }

    @Override
    protected ArrayList<Float> addValues(ArrayList<Float> values1, ArrayList<Float> values2) {
        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < values1.size(); i++)
            res.add(values1.get(i) + values2.get(i));

        return res;
    }
}
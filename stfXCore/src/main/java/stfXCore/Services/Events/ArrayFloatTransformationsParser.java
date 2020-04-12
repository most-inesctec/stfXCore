package stfXCore.Services.Events;

import stfXCore.Services.DataTypes.ArrayFloatTransformation;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class ArrayFloatTransformationsParser extends TransformationsParser<ArrayFloatTransformation> {

    private static final Float DIRECTION_THRESHOLD = (float) Math.PI / 4;


    @Override
    protected ArrayFloatTransformation getNullValue() {
        return new ArrayFloatTransformation(new ArrayList<>(Arrays.asList(0f, 0f)));
    }


    @Override
    protected boolean changeDirection(ArrayFloatTransformation transformation1, ArrayFloatTransformation transformation2) {
        float dotProduct = 0f;
        ArrayList<Float> values = transformation1.getTransformation(),
                previousValues = transformation2.getTransformation();

        for (int i = 0; i < values.size(); i++)
            dotProduct += values.get(i) * previousValues.get(i);

        // If angle between two vectors > 45º say direction changed
        return Math.acos(dotProduct / (transformation1.value() * transformation2.value())) > DIRECTION_THRESHOLD;
    }
}
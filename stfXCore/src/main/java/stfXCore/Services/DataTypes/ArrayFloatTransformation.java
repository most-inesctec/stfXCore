package stfXCore.Services.DataTypes;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayFloatTransformation extends TransformationDataType<ArrayList<Float>> {

    private static final Float DIRECTION_THRESHOLD = (float) Math.PI / 4;

    public ArrayFloatTransformation(ArrayList<Float> value) {
        super(value);
    }

    @Override
    public ArrayFloatTransformation nullValue() {
        return new ArrayFloatTransformation(new ArrayList<>(Arrays.asList(0f, 0f)));
    }

    @Override
    public boolean verifyNull() {
        for (Float value : this.value)
            if (value != 0)
                return false;
        return true;
    }

    @Override
    public float numericalValue() {
        return (float) Math.sqrt(value.stream().reduce(
                0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
    }

    @Override
    public ArrayFloatTransformation add(ArrayList<Float> value) {
        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < this.value.size(); i++)
            res.add(this.value.get(i) + value.get(i));

        return new ArrayFloatTransformation(res);
    }

    @Override
    public ArrayFloatTransformation subtract(ArrayList<Float> value) {

        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < this.value.size(); i++)
            res.add(this.value.get(i) - value.get(i));

        return new ArrayFloatTransformation(res);
    }

    @Override
    public boolean changeDirection(TransformationDataType<ArrayList<Float>> transformation) {
        float dotProduct = 0f;
        ArrayList<Float> values = transformation.getValue(),
                previousValues = this.getValue();

        for (int i = 0; i < values.size(); i++)
            dotProduct += values.get(i) * previousValues.get(i);

        // If angle between two vectors > 45º say direction changed
        return Math.acos(dotProduct / (transformation.numericalValue() * this.numericalValue())) > DIRECTION_THRESHOLD;
    }
}

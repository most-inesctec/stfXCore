package stfXCore.Services.DataTypes;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayFloatTransformation extends TransformationDataType<ArrayList<Float>> {

    private static final Float DIRECTION_THRESHOLD = (float) Math.PI / 4;

    public ArrayFloatTransformation(ArrayList<Float> transformation) {
        super(transformation);
    }

    @Override
    public ArrayFloatTransformation nullValue() {
        return new ArrayFloatTransformation(new ArrayList<>(Arrays.asList(0f, 0f)));
    }

    @Override
    public boolean verifyNull() {
        for (Float value : this.transformation)
            if (value != 0)
                return false;
        return true;
    }

    @Override
    public float value() {
        return (float) Math.sqrt(transformation.stream().reduce(
                0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
    }

    @Override
    public ArrayFloatTransformation add(ArrayList<Float> transformation) {
        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < this.transformation.size(); i++)
            res.add(this.transformation.get(i) + transformation.get(i));

        return new ArrayFloatTransformation(res);
    }

    @Override
    public ArrayFloatTransformation subtract(ArrayList<Float> transformation) {

        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < this.transformation.size(); i++)
            res.add(this.transformation.get(i) - transformation.get(i));

        return new ArrayFloatTransformation(res);
    }

    @Override
    public boolean changeDirection(TransformationDataType<ArrayList<Float>> transformation) {
        float dotProduct = 0f;
        ArrayList<Float> values = transformation.getTransformation(),
                previousValues = this.getTransformation();

        for (int i = 0; i < values.size(); i++)
            dotProduct += values.get(i) * previousValues.get(i);

        // If angle between two vectors > 45º say direction changed
        return Math.acos(dotProduct / (transformation.value() * this.value())) > DIRECTION_THRESHOLD;
    }
}

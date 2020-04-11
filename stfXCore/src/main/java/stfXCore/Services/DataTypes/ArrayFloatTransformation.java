package stfXCore.Services.DataTypes;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayFloatTransformation extends ITransformationDataType<ArrayList<Float>> {

    private static final Float DIRECTION_THRESHOLD = (float) Math.PI / 4;

    ArrayFloatTransformation(ArrayList<Float> transformation) {
        super(transformation);
    }

    @Override
    public boolean verifyNull() {
        for (Float value : this.transformation)
            if (value != 0)
                return false;
        return true;
    }

    @Override
    public ArrayList<Float> getNullValue() {
        return new ArrayList<>(Arrays.asList(0f, 0f));
    }

    private static float getAbsoluteValue(ArrayList<Float> value) {
        return (float) Math.sqrt(value.stream().reduce(
                0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
    }

    @Override
    public float getValue() {
        return getAbsoluteValue(transformation);
    }

    @Override
    public boolean changeDirection(ArrayList<Float> transformation) {
        Float dotProduct = 0f;

        for (int i = 0; i < this.transformation.size(); i++)
            dotProduct += this.transformation.get(i) * transformation.get(i);

        // If angle between two vectors > 45º say direction changed
        return Math.acos(dotProduct / (getAbsoluteValue(this.transformation) * getAbsoluteValue(transformation))) > DIRECTION_THRESHOLD;
    }

    @Override
    public ArrayList<Float> add(ArrayList<Float> transformation) {
        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < this.transformation.size(); i++)
            res.add(this.transformation.get(i) + transformation.get(i));

        return res;
    }

    @Override
    public ArrayList<Float> subtract(ArrayList<Float> transformation) {

        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < this.transformation.size(); i++)
            res.add(this.transformation.get(i) - transformation.get(i));

        return res;
    }
}

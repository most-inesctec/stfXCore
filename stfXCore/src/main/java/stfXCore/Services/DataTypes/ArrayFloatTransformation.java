package stfXCore.Services.DataTypes;

import java.util.ArrayList;
import java.util.Arrays;

public class ArrayFloatTransformation implements ITransformationDataType<ArrayList<Float>> {

    private static final Float DIRECTION_THRESHOLD = (float) Math.PI / 4;

    @Override
    public boolean verifyNull(ArrayList<Float> transformation) {
        for (Float value : transformation)
            if (value != 0)
                return false;
        return true;
    }

    @Override
    public ArrayList<Float> getNullValue() {
        return new ArrayList<>(Arrays.asList(0f, 0f));
    }

    @Override
    public float getValue(ArrayList<Float> transformation) {
        return (float) Math.sqrt(transformation.stream().reduce(
                0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
    }

    @Override
    public boolean changeDirection(ArrayList<Float> transformation1, ArrayList<Float> transformation2) {
        Float dotProduct = 0f;

        for (int i = 0; i < transformation1.size(); i++)
            dotProduct += transformation1.get(i) * transformation2.get(i);

        // If angle between two vectors > 45º say direction changed
        return Math.acos(dotProduct / (getValue(transformation1) * getValue(transformation2))) > DIRECTION_THRESHOLD;
    }

    @Override
    public ArrayList<Float> add(ArrayList<Float> transformation1, ArrayList<Float> transformation2) {
        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < transformation1.size(); i++)
            res.add(transformation1.get(i) + transformation2.get(i));

        return res;
    }

    @Override
    public ArrayList<Float> subtract(ArrayList<Float> transformation1, ArrayList<Float> transformation2) {

        ArrayList<Float> res = new ArrayList<>();

        for (int i = 0; i < transformation1.size(); i++)
            res.add(transformation1.get(i) - transformation2.get(i));

        return res;
    }
}

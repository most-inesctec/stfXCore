package stfXCore.Services.DataTypes;

import java.util.ArrayList;

public class ArrayFloatITransformation implements ITransformationDataType {

    private ArrayList<Float> values;

    public ArrayList<Float> getValues() {
        return this.values;
    }

    @Override
    public float getValue() {
        return (float) Math.sqrt(values.stream().reduce(
                0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
    }

    @Override
    public boolean verifyNull() {
        for (Float value : values)
            if (value != 0)
                return false;
        return true;
    }

    @Override
    public boolean differentDirection(ITransformationDataType t) throws Exception {
        if (t.getClass().equals(this.getClass())) {
            // TODO if angle > 90º degrees
        }

        throw new Exception("Incompatible type");
    }

    @Override
    public void add(ITransformationDataType t) {
        if (t.getClass().equals(this.getClass())) {
            ArrayList<Float> addVec = ((ArrayFloatITransformation) t).getValues();
            for(int i = 0; i < this.values.size(); ++i)
                this.values.set(i, this.values.get(i) + addVec.get(i));
        }
    }
}

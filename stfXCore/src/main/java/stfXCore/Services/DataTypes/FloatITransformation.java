package stfXCore.Services.DataTypes;

public class FloatITransformation implements ITransformationDataType {

    private Float value;

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public boolean verifyNull() {
        return value == 0;
    }

    @Override
    public boolean differentDirection(ITransformationDataType t) throws Exception {
        if (t.getClass().equals(this.getClass()))
            return (value > 0 && t.getValue() < 0) || (value < 0 && t.getValue() > 0);

        throw new Exception("Incompatible type");
    }

    @Override
    public void add(ITransformationDataType t) {
        this.value += t.getValue();
    }
}

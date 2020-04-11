package stfXCore.Services.DataTypes;

public class FloatTransformation implements ITransformationDataType<Float> {

    @Override
    public boolean verifyNull(Float value) {
        return value == 0;
    }

    @Override
    public Float getNullValue() {
        return 0f;
    }

    @Override
    public float getValue(Float value) {
        return value;
    }

    @Override
    public boolean changeDirection(Float value, Float previousValue) {
        return (value > 0 && previousValue < 0) || (value < 0 && previousValue > 0);
    }

    @Override
    public Float add(Float value1, Float value2) {
        return value1 + value2;
    }

    @Override
    public Float subtract(Float transformation1, Float transformation2) {
        return transformation1 - transformation2;
    }
}

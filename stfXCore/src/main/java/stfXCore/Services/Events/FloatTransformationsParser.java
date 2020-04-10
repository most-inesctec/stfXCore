package stfXCore.Services.Events;

public abstract class FloatTransformationsParser extends TransformationsParser<Float> {
    @Override
    protected boolean verifyNull(Float value) {
        return value == 0;
    }

    @Override
    protected Float getNullValue() {
        return 0f;
    }

    @Override
    protected Float getValue(Float value) {
        return value;
    }

    @Override
    protected boolean changeDirection(Float value, Float previousValue) {
        return (value > 0 && previousValue < 0) || (value < 0 && previousValue > 0);
    }

    @Override
    protected Float addValues(Float value1, Float value2) {
        return value1 + value2;
    }
}

package stfXCore.Services.Events;

import stfXCore.Services.DataTypes.FloatTransformation;

public abstract class FloatTransformationsParser extends TransformationsParser<FloatTransformation, Float> {

    @Override
    protected FloatTransformation getNullValue() {
        return new FloatTransformation(0f);
    }

    @Override
    protected boolean changeDirection(FloatTransformation value, FloatTransformation previousValue) {
        return (value.getValue() > 0 && previousValue.getValue() < 0) ||
                (value.getValue() < 0 && previousValue.getValue() > 0);
    }

}
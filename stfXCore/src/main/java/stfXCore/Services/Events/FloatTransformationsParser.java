package stfXCore.Services.Events;

import stfXCore.Services.DataTypes.FloatTransformation;

public abstract class FloatTransformationsParser extends TransformationsParser<FloatTransformation> {

    @Override
    protected FloatTransformation getNullValue() {
        return new FloatTransformation(0f);
    }

    @Override
    protected boolean changeDirection(FloatTransformation value, FloatTransformation previousValue) {
        return (value.value() > 0 && previousValue.value() < 0) ||
                (value.value() < 0 && previousValue.value() > 0);
    }

}
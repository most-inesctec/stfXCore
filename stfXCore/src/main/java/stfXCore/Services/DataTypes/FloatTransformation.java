package stfXCore.Services.DataTypes;

public class FloatTransformation extends TransformationDataType<Float> {

    public FloatTransformation(Float value) {
        super(value);
    }

    @Override
    public boolean verifyNull() {
        return this.value == 0;
    }

    @Override
    public FloatTransformation nullValue() {
        return new FloatTransformation(0f);
    }

    @Override
    public float numericalValue() {
        return this.value;
    }

    @Override
    public FloatTransformation add(Float value) {
        return new FloatTransformation(this.value + value);
    }

    @Override
    public FloatTransformation subtract(Float value) {
        return new FloatTransformation(this.value - value);
    }

    @Override
    public boolean changeDirection(TransformationDataType<Float> transformation) {
        return (transformation.numericalValue() > 0 && this.numericalValue() < 0) ||
                (transformation.numericalValue() < 0 && this.numericalValue() > 0);
    }
}

package stfXCore.Services.DataTypes;

public class ScaleFloatTransformation extends TransformationDataType<Float> {

    private static final float NULL_SCALE = 1f;

    public ScaleFloatTransformation(Float value) {
        super(value);
    }

    @Override
    public boolean verifyNull() {
        return this.value == NULL_SCALE;
    }

    @Override
    public ScaleFloatTransformation nullValue() {
        return new ScaleFloatTransformation(NULL_SCALE);
    }

    @Override
    public float numericalValue() {
        return this.value - NULL_SCALE;
    }

    @Override
    public ScaleFloatTransformation add(Float value) {
        return new ScaleFloatTransformation(this.value * value);
    }

    @Override
    public ScaleFloatTransformation subtract(Float value) {
        return new ScaleFloatTransformation(this.value / value);
    }

    @Override
    public boolean changeDirection(TransformationDataType<Float> transformation) {
        return (transformation.numericalValue() > NULL_SCALE && this.numericalValue() < NULL_SCALE) ||
                (transformation.numericalValue() < NULL_SCALE && this.numericalValue() > NULL_SCALE);
    }
}

package stfXCore.Services.DataTypes;

public class ScaleFloatTransformation extends TransformationDataType<Float> {

    private static final float NULL_SCALE = 1f;

    public ScaleFloatTransformation(Float transformation) {
        super(transformation);
    }

    @Override
    public boolean verifyNull() {
        return this.transformation == NULL_SCALE;
    }

    @Override
    public ScaleFloatTransformation getNullValue() {
        return new ScaleFloatTransformation(NULL_SCALE);
    }

    @Override
    public float value() {
        return this.transformation - NULL_SCALE;
    }

    @Override
    public ScaleFloatTransformation add(Float transformation) {
        return new ScaleFloatTransformation(this.transformation * transformation);
    }

    @Override
    public ScaleFloatTransformation subtract(Float transformation) {
        return new ScaleFloatTransformation(this.transformation / transformation);
    }

    @Override
    public boolean changeDirection(TransformationDataType<Float> value) {
        return (value.value() > NULL_SCALE && this.value() < NULL_SCALE) ||
                (value.value() < NULL_SCALE && this.value() > NULL_SCALE);
    }
}

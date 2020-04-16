package stfXCore.Services.DataTypes;

public class FloatTransformation extends TransformationDataType<Float> {

    public FloatTransformation(Float transformation) {
        super(transformation);
    }

    @Override
    public boolean verifyNull() {
        return this.transformation == 0;
    }

    @Override
    public FloatTransformation nullValue() {
        return new FloatTransformation(0f);
    }

    @Override
    public float value() {
        return this.transformation;
    }

    @Override
    public FloatTransformation add(Float transformation) {
        return new FloatTransformation(this.transformation + transformation);
    }

    @Override
    public FloatTransformation subtract(Float transformation) {
        return new FloatTransformation(this.transformation - transformation);
    }

    @Override
    public boolean changeDirection(TransformationDataType<Float> value) {
        return (value.value() > 0 && this.value() < 0) ||
                (value.value() < 0 && this.value() > 0);
    }
}

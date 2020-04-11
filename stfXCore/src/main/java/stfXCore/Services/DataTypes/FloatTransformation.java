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
    public float getValue() {
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
}

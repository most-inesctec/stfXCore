package stfXCore.Services.DataTypes;

public class FloatTransformation extends TransformationDataType<Float> {

    FloatTransformation(Float transformation) {
        super(transformation);
    }

    @Override
    public boolean verifyNull() {
        return this.transformation == 0;
    }

    @Override
    public Float getNullValue() {
        return 0f;
    }

    @Override
    public float getValue() {
        return this.transformation;
    }

    @Override
    public boolean changeDirection(Float transformation) {
        return (this.transformation > 0 && transformation < 0) || (this.transformation < 0 && transformation > 0);
    }

    @Override
    public Float add(Float transformation) {
        return this.transformation + transformation;
    }

    @Override
    public Float subtract(Float transformation) {
        return this.transformation - transformation;
    }
}

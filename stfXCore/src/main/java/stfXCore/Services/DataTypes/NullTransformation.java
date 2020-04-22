package stfXCore.Services.DataTypes;

public class NullTransformation extends TransformationDataType<Object> {

    public NullTransformation() {
        super(null);
    }

    @Override
    public boolean verifyNull() {
        return this.transformation == null;
    }

    @Override
    public NullTransformation nullValue() {
        return this;
    }

    @Override
    public float value() {
        return 0f;
    }

    @Override
    public NullTransformation add(Object transformation) {
        return nullValue();
    }

    @Override
    public NullTransformation subtract(Object transformation) {
        return nullValue();
    }

    @Override
    public boolean changeDirection(TransformationDataType<Object> value) {
        return false;
    }
}

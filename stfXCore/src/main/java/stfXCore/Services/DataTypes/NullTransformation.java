package stfXCore.Services.DataTypes;

public class NullTransformation extends TransformationDataType<Object> {

    public NullTransformation() {
        super(null);
    }

    @Override
    public boolean verifyNull() {
        return this.value == null;
    }

    @Override
    public NullTransformation nullValue() {
        return this;
    }

    @Override
    public float numericalValue() {
        return 0f;
    }

    @Override
    public NullTransformation add(Object value) {
        return nullValue();
    }

    @Override
    public NullTransformation subtract(Object value) {
        return nullValue();
    }

    @Override
    public boolean changeDirection(TransformationDataType<Object> transformation) {
        return false;
    }
}

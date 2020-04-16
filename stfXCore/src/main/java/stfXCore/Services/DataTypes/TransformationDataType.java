package stfXCore.Services.DataTypes;

public abstract class TransformationDataType<T> {

    protected T transformation;

    TransformationDataType(T transformation) {
        this.transformation = transformation;
    }

    public abstract boolean verifyNull();

    public abstract TransformationDataType<T> nullValue();

    public T getTransformation() {
        return transformation;
    }

    public abstract float value();

    public abstract TransformationDataType<T> add(T transformation);

    public abstract TransformationDataType<T> subtract(T transformation);

    public abstract boolean changeDirection(TransformationDataType<T> value);
}

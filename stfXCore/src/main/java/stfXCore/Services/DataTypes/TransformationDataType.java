package stfXCore.Services.DataTypes;

public abstract class TransformationDataType<T> {

    protected T value;

    TransformationDataType(T value) {
        this.value = value;
    }

    public abstract boolean verifyNull();

    public abstract TransformationDataType<T> nullValue();

    public T getTransformation() {
        return value;
    }

    public abstract float numericalValue();

    public abstract TransformationDataType<T> add(T value);

    public abstract TransformationDataType<T> subtract(T value);

    public abstract boolean changeDirection(TransformationDataType<T> transformation);
}

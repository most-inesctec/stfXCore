package stfXCore.Services.DataTypes;

public abstract class TransformationDataType<T> {

    protected T transformation;

    TransformationDataType(T transformation) {
        this.transformation = transformation;
    }

    public abstract boolean verifyNull();

    public T getTransformation() {
        return transformation;
    }

    public abstract float getValue();

    public abstract TransformationDataType<T> add(T transformation);

    public abstract TransformationDataType<T> subtract(T transformation);
}

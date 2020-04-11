package stfXCore.Services.DataTypes;

public abstract class TransformationDataType<T> {

    protected T transformation;

    TransformationDataType(T transformation) {
        this.transformation = transformation;
    }

    public abstract boolean verifyNull();

    public abstract T getNullValue();

    public abstract float getValue();

    public abstract boolean changeDirection(T transformation);

    public abstract T add(T transformation);

    public abstract T subtract(T transformation);
}

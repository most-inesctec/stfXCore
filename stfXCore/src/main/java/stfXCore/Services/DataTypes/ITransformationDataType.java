package stfXCore.Services.DataTypes;

public abstract class ITransformationDataType<T> {

    protected T transformation;

    ITransformationDataType(T transformation) {
        this.transformation = transformation;
    }

    public abstract boolean verifyNull();

    public abstract T getNullValue();

    public abstract float getValue();

    public abstract boolean changeDirection(T transformation);

    public abstract T add(T transformation);

    public abstract T subtract(T transformation);
}

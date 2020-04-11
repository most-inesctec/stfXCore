package stfXCore.Services.DataTypes;

public interface ITransformationDataType<T> {

    public boolean verifyNull(T transformation);

    public T getNullValue();

    public float getValue(T transformation);

    public boolean changeDirection(T transformation1, T transformation2);

    public T add(T transformation1, T transformation2);

    public T subtract(T transformation1, T transformation2);
}

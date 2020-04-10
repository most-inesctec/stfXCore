package stfXCore.Services.DataTypes;

public interface ITransformationDataType {

    public float getValue();

    public boolean verifyNull();

    public <T extends ITransformationDataType> boolean differentDirection(T t) throws Exception;

    public void add(ITransformationDataType t);
}

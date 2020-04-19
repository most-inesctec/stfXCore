package stfXCore.Services.DataTypes;

public class LongTransformation extends TransformationDataType<Long> {

    public LongTransformation(Long transformation) {
        super(transformation);
    }

    @Override
    public boolean verifyNull() {
        return this.transformation == 0L;
    }

    @Override
    public LongTransformation nullValue() {
        return new LongTransformation(0L);
    }

    @Override
    public float value() {
        return this.transformation;
    }

    @Override
    public LongTransformation add(Long transformation) {
        return new LongTransformation(this.transformation + transformation);
    }

    @Override
    public LongTransformation subtract(Long transformation) {
        return new LongTransformation(this.transformation - transformation);
    }

    @Override
    public boolean changeDirection(TransformationDataType<Long> value) {
        return (value.value() > 0 && this.value() < 0) ||
                (value.value() < 0 && this.value() > 0);
    }
}

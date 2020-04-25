package stfXCore.Services.DataTypes;

public class LongTransformation extends TransformationDataType<Long> {

    public LongTransformation(Long value) {
        super(value);
    }

    @Override
    public boolean verifyNull() {
        return this.value == 0L;
    }

    @Override
    public LongTransformation nullValue() {
        return new LongTransformation(0L);
    }

    @Override
    public float numericalValue() {
        return this.value;
    }

    @Override
    public LongTransformation add(Long value) {
        return new LongTransformation(this.value + value);
    }

    @Override
    public LongTransformation subtract(Long value) {
        return new LongTransformation(this.value - value);
    }

    @Override
    public boolean changeDirection(TransformationDataType<Long> transformation) {
        return (transformation.numericalValue() > 0 && this.numericalValue() < 0) ||
                (transformation.numericalValue() < 0 && this.numericalValue() > 0);
    }
}

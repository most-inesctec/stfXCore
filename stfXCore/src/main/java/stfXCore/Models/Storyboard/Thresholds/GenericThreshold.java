package stfXCore.Models.Storyboard.Thresholds;

import lombok.Data;

@Data
public abstract class GenericThreshold<T> {

    private T delta;

    private T directedAcc;

    private T absoluteAcc;

//    public abstract Float typeParser(T value);
}

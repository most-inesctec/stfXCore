package stfXCore.Services.Events;

import stfXCore.Services.DataTypes.NullTransformation;

public class UnimportantEvent extends Event<NullTransformation> {
    public UnimportantEvent() {
        super(Transformation.UNIMPORTANT);
    }
}

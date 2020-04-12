package stfXCore.Services.Parsers;

import stfXCore.Services.DataTypes.ScaleFloatTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UniformScaleParser extends TransformationsParser<ScaleFloatTransformation> {

    UniformScaleParser() {
    }

    @Override
    public ArrayList<Event<?>> parse(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, @NotNull GenericThreshold<Float> threshold) {
        return filterThreshold(
                rigidTransformations.stream().map(
                        pair -> new Pair<>(pair.getFirst(), new ScaleFloatTransformation(pair.getSecond().getScale())))
                        .collect(Collectors.toList()),
                threshold,
                Event.Transformation.UNIFORM_SCALE);
    }
}

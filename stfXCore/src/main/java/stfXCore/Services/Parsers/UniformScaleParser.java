package stfXCore.Services.Parsers;

import stfXCore.Models.Storyboard.Events.Event;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UniformScaleParser extends TransformationsParser<FloatTransformation> {

    UniformScaleParser() {
    }

    @Override
    public ArrayList<Event<?>> parse(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, @NotNull GenericThreshold<Float> threshold) {
        return filterThreshold(
                rigidTransformations.stream().map(
                        pair -> new Pair<>(pair.getFirst(), new FloatTransformation(pair.getSecond().getScale() - 1)))
                        .collect(Collectors.toList()),
                threshold,
                Event.Transformation.UNIFORM_SCALE);
    }
}

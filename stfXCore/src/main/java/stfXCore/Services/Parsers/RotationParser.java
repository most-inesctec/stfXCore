package stfXCore.Services.Parsers;

import stfXCore.Services.Events.Event;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RotationParser extends TransformationsParser<FloatTransformation> {

    RotationParser() {
    }

    @Override
    public ArrayList<Event<?>> parse(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, GenericThreshold<Float> threshold) {
        return filterThreshold(
                rigidTransformations.stream().map(
                        pair -> new Pair<>(pair.getFirst(), new FloatTransformation(pair.getSecond().getRotation())))
                        .collect(Collectors.toList()),
                threshold,
                Event.Transformation.ROTATION);
    }
}

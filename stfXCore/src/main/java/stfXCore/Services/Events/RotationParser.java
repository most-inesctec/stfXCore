package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Events.Event;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RotationParser extends FloatTransformationsParser {

    RotationParser() {
    }

    public ArrayList<Event> parse(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, GenericThreshold<Float> threshold) {
        return filterThreshold(
                rigidTransformations.stream().map(
                        pair -> new Pair<Snapshot, Float>(pair.getFirst(), pair.getSecond().getRotation()))
                        .collect(Collectors.toList()),
                threshold,
                Event.Transformation.ROTATION);
    }
}

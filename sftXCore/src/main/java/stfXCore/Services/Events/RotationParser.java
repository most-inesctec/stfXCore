package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RotationParser extends EventParser {

    RotationParser() {
    }

    public ArrayList<Event> parse(@NotNull ArrayList<RigidTransformation> rigidTransformations, GenericThreshold<Float> threshold) {
        return new ArrayList<>(filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getRotation()).collect(Collectors.toList()),
                threshold,
                Event.Transformation.ROTATION));
    }
}

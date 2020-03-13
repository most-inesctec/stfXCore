package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UniformScaleParser extends EventParser {

    UniformScaleParser() {
    }

    public ArrayList<Event> parse(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull GenericThreshold<Float> threshold) {
        return new ArrayList<>(filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getScale() - 1).collect(Collectors.toList()),
                threshold,
                Event.Transformation.UNIFORM_SCALE));
    }
}

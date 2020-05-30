package stfXCore.Services.Parsers;

import stfXCore.Models.Transformations.SnapshotTransformationPair;
import stfXCore.Services.DataTypes.ScaleFloatTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Models.Thresholds.GenericThreshold;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UniformScaleParser extends TransformationsParser<ScaleFloatTransformation> {

    UniformScaleParser(GenericThreshold<Float> threshold) {
        super(threshold);
    }

    @Override
    public ArrayList<Event<?>> parse(@NotNull List<SnapshotTransformationPair> rigidTransformations) {
        return filterThreshold(
                rigidTransformations.stream().map(
                        pair -> new Pair<>(pair.getFirst(), new ScaleFloatTransformation(pair.getSecond().getScale())))
                        .collect(Collectors.toList()),
                Event.Transformation.UNIFORM_SCALE);
    }
}

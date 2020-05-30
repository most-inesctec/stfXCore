package stfXCore.Services.Parsers;

import stfXCore.Models.Transformations.SnapshotTransformationPair;
import stfXCore.Services.Events.Event;
import stfXCore.Models.Thresholds.GenericThreshold;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RotationParser extends TransformationsParser<FloatTransformation> {

    RotationParser(GenericThreshold<Float> threshold) {
        super(threshold);
    }

    @Override
    public ArrayList<Event<?>> parse(@NotNull List<SnapshotTransformationPair> rigidTransformations) {
        return filterThreshold(
                rigidTransformations.stream().map(
                        pair -> new Pair<>(pair.getFirst(), new FloatTransformation(pair.getSecond().getRotation())))
                        .collect(Collectors.toList()),
                Event.Transformation.ROTATION);
    }
}

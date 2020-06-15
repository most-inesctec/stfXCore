package stfXCore.Services.Parsers;

import stfXCore.Models.SnapshotPair;
import stfXCore.Models.Thresholds.ThresholdParameters;
import stfXCore.Models.Transformations.RigidTransformation;
import stfXCore.Models.Transformations.SnapshotTransformationPair;
import stfXCore.Services.DataTypes.ArrayFloatTransformation;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Services.DataTypes.LongTransformation;
import stfXCore.Services.DataTypes.ScaleFloatTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static stfXCore.Services.Events.Event.Transformation.IMMUTABILITY;

public class ImmutabilityParser implements ITransformationParser {

    private ThresholdParameters thresholds;

    Long counter;

    ArrayList<Pair<Long, LongTransformation>> representations;

    ImmutabilityParser(ThresholdParameters threshold) {
        this.thresholds = threshold;
    }

    private void resetCounters() {
        counter = 0L;
        representations = new ArrayList<>();
    }

    protected ArrayList<Event<?>> filterThreshold(List<Pair<SnapshotPair, Boolean>> transformations) {
        ArrayList<Event<?>> eventsOfInterest = new ArrayList<>();
        Long threshold = thresholds.getImmutability();
        resetCounters();

        if (threshold != null) {
            for (Pair<SnapshotPair, Boolean> transformation : transformations) {
                if (transformation.getSecond()) {
                    SnapshotPair s = transformation.getFirst();

                    if (representations.size() == 0)
                        representations.add(new Pair<Long, LongTransformation>(
                                s.getX().getTimestamp(),
                                new LongTransformation(0L)));

                    counter += s.getY().getTimestamp() - s.getX().getTimestamp();
                    representations.add(new Pair<Long, LongTransformation>(
                            s.getY().getTimestamp(),
                            new LongTransformation(counter)));
                } else {
                    resetCounters();
                    continue;
                }

                if (counter >= threshold) {
                    eventsOfInterest.add(
                            new Event<LongTransformation>(IMMUTABILITY)
                                    .setValues(representations));
                    resetCounters();
                }
            }
        }

        return eventsOfInterest;
    }

    // TODO this deserves a strong refactor
    private List<SnapshotTransformationPair> applyNoiseFilters(List<SnapshotTransformationPair> rigidTransformations) {
        if (thresholds.getImmutability() == null)
            return rigidTransformations;

        List<Pair<SnapshotPair, ArrayFloatTransformation>> translations = new ArrayList<>();
        List<Pair<SnapshotPair, ScaleFloatTransformation>> scales = new ArrayList<>();
        List<Pair<SnapshotPair, FloatTransformation>> rotations = new ArrayList<>();

        // Diving into list of correspondent TransformationsTypes
        for (SnapshotTransformationPair transformation : rigidTransformations) {
            SnapshotPair s = transformation.getFirst();
            RigidTransformation t = transformation.getSecond();

            translations.add(new Pair<>(s, new ArrayFloatTransformation(t.getTranslation())));
            scales.add(new Pair<>(s, new ScaleFloatTransformation(t.getScale())));
            rotations.add(new Pair<>(s, new FloatTransformation(t.getRotation())));
        }

        // Parse noise
        if (thresholds.getTranslation() != null)
            translations = new NoiseEpsilonParser<ArrayFloatTransformation>(thresholds.getTranslation()).filterNoise(translations);
        if (thresholds.getScale() != null)
            scales = new NoiseEpsilonParser<ScaleFloatTransformation>(thresholds.getScale()).filterNoise(scales);
        if (thresholds.getRotation() != null)
            rotations = new NoiseEpsilonParser<FloatTransformation>(thresholds.getRotation()).filterNoise(rotations);

        // Creating new RigidTransformations
        List<SnapshotTransformationPair> filtered = new ArrayList<>();
        for (int i = 0; i < translations.size(); ++i) {
            filtered.add(new SnapshotTransformationPair(
                    translations.get(i).getFirst(),
                    new RigidTransformation(
                            translations.get(i).getSecond().getTransformation(),
                            scales.get(i).getSecond().getTransformation(),
                            rotations.get(i).getSecond().getTransformation())));
        }
        return filtered;
    }

    @Override
    public ArrayList<Event<?>> parse(@NotNull List<SnapshotTransformationPair> rigidTransformations) {
        return filterThreshold(
                applyNoiseFilters(rigidTransformations).stream().map(
                        pair -> new Pair<>(pair.getFirst(), pair.getSecond().isNull()))
                        .collect(Collectors.toList()));
    }
}

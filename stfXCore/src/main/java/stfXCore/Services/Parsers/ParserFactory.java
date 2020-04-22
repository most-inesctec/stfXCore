package stfXCore.Services.Parsers;

import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;

public class ParserFactory {

    ArrayList<Pair<Snapshot, RigidTransformation>> transformations;

    ThresholdParameters thresholds;

    public ParserFactory(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> transformations, @NotNull ThresholdParameters thresholds) {
        this.transformations = transformations;
        this.thresholds = thresholds;
    }

    public ParserFactory restrictInterval(Long lowerBound, Long upperBound) {
        ArrayList<Pair<Snapshot, RigidTransformation>> trimmedTransformations = new ArrayList<>();
        for (Pair<Snapshot, RigidTransformation> pair : transformations) {
            if (pair.getFirst().getX().getTimestamp() >= lowerBound &&
                    pair.getFirst().getY().getTimestamp() <= upperBound)
                trimmedTransformations.add(pair);
        }
        this.transformations = trimmedTransformations;
        return this;
    }

    public ArrayList<Event<?>> parseTransformations() {
        ArrayList<Event<?>> eventsOfInterest = new ArrayList<>();

        // TODO: Parsing can be concurrent
        if (thresholds.getTranslation() != null)
            eventsOfInterest.addAll(
                    new TranslationParser().parse(transformations, thresholds.getTranslation()));

        if (thresholds.getRotation() != null)
            eventsOfInterest.addAll(
                    new RotationParser().parse(transformations, thresholds.getRotation()));

        if (thresholds.getScale() != null)
            eventsOfInterest.addAll(
                    new UniformScaleParser().parse(transformations, thresholds.getScale()));

        if (thresholds.getImmutability() != null)
            eventsOfInterest.addAll(
                    new ImmutabilityParser().parse(transformations, thresholds.getImmutability()));

        return eventsOfInterest;
    }
}

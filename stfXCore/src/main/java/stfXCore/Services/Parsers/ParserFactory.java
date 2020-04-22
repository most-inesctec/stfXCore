package stfXCore.Services.Parsers;

import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.concurrent.*;

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

    public ConcurrentLinkedQueue<Event<?>> parseTransformations() {
        ITransformationParser[] concurrentParsers = {
                new TranslationParser(thresholds.getTranslation()),
                new RotationParser(thresholds.getRotation()),
                new UniformScaleParser(thresholds.getScale()),
                new ImmutabilityParser(thresholds.getImmutability())
        };

        ExecutorService service = Executors.newCachedThreadPool();
        ArrayList<CompletableFuture<Boolean>> futures = new ArrayList<>();
        ConcurrentLinkedQueue<Event<?>> eventsOfInterest = new ConcurrentLinkedQueue<>();

        for (ITransformationParser parser : concurrentParsers) {
            futures.add(
                    CompletableFuture.supplyAsync(() ->
                            eventsOfInterest.addAll(parser.parse(transformations)), service));
        }

        try {
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futures.toArray(new CompletableFuture[futures.size()]));
            allFutures.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return eventsOfInterest;
    }
}

package stfXCore.Services.Parsers;

import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Models.Storyboard.Transformations.SnapshotTransformationPair;
import stfXCore.Services.Events.Event;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ParserFactory {

    List<SnapshotTransformationPair> transformations;

    ThresholdParameters thresholds;

    public ParserFactory(@NotNull List<SnapshotTransformationPair> transformations, @NotNull ThresholdParameters thresholds) {
        this.transformations = transformations;
        this.thresholds = thresholds;
    }

    public ParserFactory restrictInterval(Long lowerBound, Long upperBound) {
        ArrayList<SnapshotTransformationPair> trimmedTransformations = new ArrayList<>();
        for (SnapshotTransformationPair pair : transformations) {
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
                new ImmutabilityParser(thresholds)
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

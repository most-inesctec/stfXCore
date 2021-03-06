package stfXCore.Services.Parsers;

import stfXCore.Services.Events.Event;
import stfXCore.Models.SnapshotPair;
import stfXCore.Models.Snapshot;
import stfXCore.Models.Thresholds.GenericThreshold;
import stfXCore.Services.DataTypes.TransformationDataType;
import stfXCore.Utils.Pair;

import java.util.*;

public abstract class TransformationsParser<T extends TransformationDataType> extends NoiseEpsilonParser<T> implements ITransformationParser {
    /**
     * First Element are timestamps
     * Second Element are triggerValues
     */
    protected ArrayList<Pair<Long, T>> accDirected;
    protected ArrayList<Pair<Long, T>> accAbsolute;
    protected Float accAbsoluteValue;

    TransformationsParser(GenericThreshold<Float> threshold) {
        super(threshold);
    }

    private void resetDeltas() {
        accDirected = null;
        accAbsolute = null;
        accAbsoluteValue = 0f;
    }

    private boolean verifyNullTransformation(ArrayList<Pair<Long, T>> accumulator, T transformation) {
        return accumulator == null && transformation.verifyNull();
    }

    private Pair<Long, T> createPair(Snapshot snapshot, T increment) {
        return new Pair<Long, T>(snapshot.getTimestamp(), increment);
    }

    private T getLastSecond(ArrayList<Pair<Long, T>> list) {
        return list.get(list.size() - 1).getSecond();
    }

    protected Event<T> computeDelta(Pair<SnapshotPair, T> transformation, Float threshold, Event.Transformation type) {
        if (Math.abs(transformation.getSecond().numericalValue()) >= threshold) {
            ArrayList<Pair<Long, T>> values = new ArrayList<>();
            values.add(createPair(transformation.getFirst().getX(), (T) transformation.getSecond().nullValue()));
            values.add(createPair(transformation.getFirst().getY(), transformation.getSecond()));
            return new Event<T>(Event.ThresholdTrigger.DELTA, type).setValues(values);
        }

        return null;
    }

    protected Event<T> computeAccDirected(Pair<SnapshotPair, T> transformation, Float threshold, Event.Transformation type) {
        T transformationValue = transformation.getSecond();

        if (verifyNullTransformation(accDirected, transformationValue))
            return null;

        if (accDirected == null || transformationValue.changeDirection(getLastSecond(accDirected))) { //changeDirection(getLastSecond(accDirected), transformationValue)) {
            accDirected = new ArrayList<>();
            accDirected.add(createPair(transformation.getFirst().getX(), (T) transformationValue.nullValue()));
            accDirected.add(createPair(transformation.getFirst().getY(), transformationValue));
        } else
            accDirected.add(createPair(
                    transformation.getFirst().getY(),
                    (T) getLastSecond(accDirected).add(transformationValue.getTransformation())));

        if (Math.abs(getLastSecond(accDirected).numericalValue()) >= threshold)
            return new Event<T>(Event.ThresholdTrigger.DIRECTED_ACC, type).setValues(accDirected);

        return null;
    }

    protected Event<T> computeAccAbsolute(Pair<SnapshotPair, T> transformation, Float threshold, Event.Transformation type) {
        T transformationValue = transformation.getSecond();

        if (verifyNullTransformation(accAbsolute, transformationValue))
            return null;

        if (accAbsolute == null) {
            accAbsolute = new ArrayList<>();
            accAbsolute.add(createPair(transformation.getFirst().getX(), (T) transformationValue.nullValue()));
            accAbsolute.add(createPair(transformation.getFirst().getY(), transformationValue));
            accAbsoluteValue = Math.abs(transformationValue.numericalValue());
        } else {
            accAbsolute.add(createPair(
                    transformation.getFirst().getY(),
                    (T) getLastSecond(accAbsolute).add(transformationValue.getTransformation())));
            accAbsoluteValue += Math.abs(transformationValue.numericalValue());
        }

        if (accAbsoluteValue >= threshold)
            return new Event<T>(Event.ThresholdTrigger.ABSOLUTE_ACC, type).setValues(accAbsolute);

        return null;
    }

    /**
     * Single processing of one threshold. The process is similar to all thresholds
     *
     * @param transformations The array to be processed, containing the the transformation values
     * @param threshold       The thresholds to use
     * @param type            The type of the transformation being analyzed
     */
    protected ArrayList<Event<?>> filterThreshold(List<Pair<SnapshotPair, T>> transformations, Event.Transformation type) {
        ArrayList<Event<?>> eventsOfInterest = new ArrayList<>();
        Event<T> event;

        if (threshold == null)
            return eventsOfInterest;

        for (Pair<SnapshotPair, T> transformation : filterNoise(transformations)) {
            if (threshold.getDelta() != null) {
                event = computeDelta(transformation, threshold.getDelta(), type);
                if (event != null) {
                    eventsOfInterest.add(event);
                    resetDeltas();
                    continue;
                }
            }

            if (threshold.getDirectedAcc() != null) {
                event = computeAccDirected(transformation, threshold.getDirectedAcc(), type);
                if (event != null) {
                    eventsOfInterest.add(event);
                    resetDeltas();
                    continue;
                }
            }

            if (threshold.getAbsoluteAcc() != null) {
                event = computeAccAbsolute(transformation, threshold.getAbsoluteAcc(), type);
                if (event != null) {
                    eventsOfInterest.add(event);
                    resetDeltas();
                }
            }
        }

        return eventsOfInterest;
    }
}

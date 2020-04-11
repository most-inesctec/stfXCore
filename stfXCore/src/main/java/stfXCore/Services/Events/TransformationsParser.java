package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Events.Event;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.State;
import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.*;

public abstract class TransformationsParser<T> {

    /**
     * First Element are timestamps
     * Second Element are triggerValues
     */
    protected ArrayList<Pair<Float, T>> accDirected;
    protected ArrayList<Pair<Float, T>> accAbsolute;
    protected Float accAbsoluteValue;

    TransformationsParser() {
    }

    private void resetDeltas() {
        accDirected = null;
        accAbsolute = null;
        accAbsoluteValue = 0f;
    }

    protected abstract boolean verifyNull(T value);

    protected abstract T getNullValue();

    protected abstract Float getValue(T value);

    protected abstract boolean changeDirection(T value, T previousValue);

    protected abstract T addValues(T value1, T value2);

    private boolean verifyNullTransformation(ArrayList<Pair<Float, T>> accumulator, T transformation) {
        return accumulator == null && verifyNull(transformation);
    }

    private Pair<Float, Float> getValue(State state, Float increment) {
        return new Pair<Float, Float>(state.getTimestamp(), increment);
    }

    private T getLastSecond(ArrayList<Pair<Float, T>> list) {
        return list.get(list.size() - 1).getSecond();
    }

    protected Event<T> computeDelta(Pair<Snapshot, T> transformation, Float threshold, Event.Transformation type) {
        if (Math.abs(getValue(transformation.getSecond())) >= threshold) {
            ArrayList<Pair<Float, T>> values = new ArrayList<>();
            values.add(getValue(transformation.getFirst().getX(), getNullValue()));
            values.add(getValue(transformation.getFirst().getY(), transformation.getSecond()));
            return new Event<T>(Event.ThresholdTrigger.DELTA, type).setValues(values);
        }

        return null;
    }

    protected Event<T> computeAccDirected(Pair<Snapshot, T> transformation, Float threshold, Event.Transformation type) {
        T transformationValue = transformation.getSecond();

        if (verifyNullTransformation(accDirected, transformationValue))
            return null;

        if (accDirected == null || changeDirection(transformationValue, getLastSecond(accDirected))) {
            accDirected = new ArrayList<>();
            accDirected.add(getValue(transformation.getFirst().getX(), getNullValue()));
            accDirected.add(getValue(transformation.getFirst().getY(), transformationValue));
        } else
            accDirected.add(getValue(
                    transformation.getFirst().getY(),
                    addValues(getLastSecond(accDirected), transformationValue)));

        if (Math.abs(getValue(getLastSecond(accDirected))) >= threshold)
            return new Event<T>(Event.ThresholdTrigger.DIRECTED_ACC, type).setValues(accDirected);

        return null;
    }

    protected Event<T> computeAccAbsolute(Pair<Snapshot, T> transformation, Float threshold, Event.Transformation type) {
        T transformationValue = transformation.getSecond();

        if (verifyNullTransformation(accAbsolute, transformationValue))
            return null;

        if (accAbsolute == null) {
            accAbsolute = new ArrayList<>();
            accAbsolute.add(getValue(transformation.getFirst().getX(), 0f));
            accAbsolute.add(getValue(transformation.getFirst().getY(), transformationValue));
            accAbsoluteValue = Math.abs(getValue(transformationValue));
        } else {
            accAbsolute.add(getValue(
                    transformation.getFirst().getY(),
                    addValues(getLastSecond(accAbsolute), transformationValue)));
            accAbsoluteValue += Math.abs(getValue(transformationValue));
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
    protected ArrayList<Event<T>> filterThreshold(List<Pair<Snapshot, T>> transformations, GenericThreshold<Float> threshold, Event.Transformation type) {
        ArrayList<Event<T>> eventsOfInterest = new ArrayList<>();

        for (Pair<Snapshot, T> transformation : transformations) {
            Event<T> event = computeDelta(transformation, threshold.getDelta(), type);
            if (event != null) {
                eventsOfInterest.add(event);
                resetDeltas();
                continue;
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

    protected abstract ArrayList<Event<T>> parse(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, @NotNull GenericThreshold<Float> threshold);

    public static ArrayList<Event> parseTransformations(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, @NotNull ThresholdParameters thresholds) {
        ArrayList<Event> eventsOfInterest = new ArrayList<>();

        // TODO: Parsing can be concurrent
        if (thresholds.getTranslation() != null)
            eventsOfInterest.addAll(
                    new TranslationParser().parse(rigidTransformations, thresholds.getTranslation()));

        if (thresholds.getRotation() != null)
            eventsOfInterest.addAll(
                    new RotationParser().parse(rigidTransformations, thresholds.getRotation()));

        if (thresholds.getScale() != null)
            eventsOfInterest.addAll(
                    new UniformScaleParser().parse(rigidTransformations, thresholds.getScale()));

        return eventsOfInterest;
    }
}

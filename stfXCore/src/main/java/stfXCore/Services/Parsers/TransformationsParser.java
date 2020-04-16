package stfXCore.Services.Parsers;

import stfXCore.Services.Events.Event;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.State;
import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Services.DataTypes.TransformationDataType;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.*;

public abstract class TransformationsParser<T extends TransformationDataType> {

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

    private boolean verifyNullTransformation(ArrayList<Pair<Float, T>> accumulator, T transformation) {
        return accumulator == null && transformation.verifyNull();
    }

    private Pair<Float, T> createPair(State state, T increment) {
        return new Pair<Float, T>(state.getTimestamp(), increment);
    }

    private T getLastSecond(ArrayList<Pair<Float, T>> list) {
        return list.get(list.size() - 1).getSecond();
    }

    protected Event<T> computeDelta(Pair<Snapshot, T> transformation, Float threshold, Event.Transformation type) {
        if (Math.abs(transformation.getSecond().value()) >= threshold) {
            ArrayList<Pair<Float, T>> values = new ArrayList<>();
            values.add(createPair(transformation.getFirst().getX(), (T) transformation.getSecond().nullValue()));
            values.add(createPair(transformation.getFirst().getY(), transformation.getSecond()));
            return new Event<T>(Event.ThresholdTrigger.DELTA, type).setValues(values);
        }

        return null;
    }

    protected Event<T> computeAccDirected(Pair<Snapshot, T> transformation, Float threshold, Event.Transformation type) {
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

        if (Math.abs(getLastSecond(accDirected).value()) >= threshold)
            return new Event<T>(Event.ThresholdTrigger.DIRECTED_ACC, type).setValues(accDirected);

        return null;
    }

    protected Event<T> computeAccAbsolute(Pair<Snapshot, T> transformation, Float threshold, Event.Transformation type) {
        T transformationValue = transformation.getSecond();

        if (verifyNullTransformation(accAbsolute, transformationValue))
            return null;

        if (accAbsolute == null) {
            accAbsolute = new ArrayList<>();
            accAbsolute.add(createPair(transformation.getFirst().getX(), (T) transformationValue.nullValue()));
            accAbsolute.add(createPair(transformation.getFirst().getY(), transformationValue));
            accAbsoluteValue = Math.abs(transformationValue.value());
        } else {
            accAbsolute.add(createPair(
                    transformation.getFirst().getY(),
                    (T) getLastSecond(accAbsolute).add(transformationValue.getTransformation())));
            accAbsoluteValue += Math.abs(transformationValue.value());
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
    protected ArrayList<Event<?>> filterThreshold(List<Pair<Snapshot, T>> transformations, GenericThreshold<Float> threshold, Event.Transformation type) {
        ArrayList<Event<?>> eventsOfInterest = new ArrayList<>();

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

    protected abstract ArrayList<Event<?>> parse(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, @NotNull GenericThreshold<Float> threshold);

    public static ArrayList<Event<?>> parseTransformations(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations, @NotNull ThresholdParameters thresholds) {
        ArrayList<Event<?>> eventsOfInterest = new ArrayList<>();

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

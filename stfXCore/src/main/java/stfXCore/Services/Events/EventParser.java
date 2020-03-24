package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.State;
import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Services.TemporalFrames.FramedDataset;
import stfXCore.Services.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.*;

public abstract class EventParser {

    protected enum Direction {
        FORWARD,
        BACKWARD,
        NONE
    }

    protected Direction direction = Direction.NONE;
    protected Pair<ArrayList<State>, Float> accDirected;
    protected Pair<ArrayList<State>, Float> accAbsolute;


    EventParser() {
    }

    private void resetDeltas() {
        direction = Direction.NONE;
        accDirected = null;
        accAbsolute = null;
    }

    private boolean verifyNullTransformation(Pair<ArrayList<State>, Float> accumulator, Float transformation) {
        return accumulator == null && transformation == 0;
    }

    protected Event computeDelta(Pair<Snapshot, Float> transformation, Float threshold, Event.Transformation type) {
        if (transformation.getSecond() >= threshold) {
            Event event = new Event(Event.ThresholdTrigger.DELTA, type, transformation.getSecond());
            event.setPhenomena(transformation.getFirst().getStates());
            return event;
        }
        return null;
    }

    protected Event computeAccDirected(Pair<Snapshot, Float> transformation, Float threshold, Event.Transformation type) {
        Float transformationValue = transformation.getSecond();

        if (verifyNullTransformation(accDirected, transformationValue))
            return null;

        // If direction is different from current, reset
        if (!((direction == Direction.FORWARD && transformationValue > 0) ||
                (direction == Direction.BACKWARD && transformationValue < 0))) {
            direction = transformationValue > 0 ? Direction.FORWARD : Direction.BACKWARD;
            ArrayList<State> states = new ArrayList<>();
            states.add(transformation.getFirst().getX());
            accDirected = new Pair<>(states, transformationValue);
        } else {
            accDirected.getFirst().add(transformation.getFirst().getX());
            accDirected.setSecond(accDirected.getSecond() + transformationValue);
        }


        if (Math.abs(accDirected.getSecond()) >= threshold) {
            Event event = new Event(
                    Event.ThresholdTrigger.DIRECTED_ACC,
                    type,
                    accDirected.getSecond());
            accDirected.getFirst().add(transformation.getFirst().getY());
            event.setPhenomena(accDirected.getFirst());
            return event;
        }
        return null;
    }

    protected Event computeAccAbsolute(Pair<Snapshot, Float> transformation, Float threshold, Event.Transformation type) {
        Float transformationValue = transformation.getSecond();

        if (verifyNullTransformation(accAbsolute, transformationValue))
            return null;

        if (accAbsolute == null) {
            ArrayList<State> states = new ArrayList<>();
            states.add(transformation.getFirst().getX());
            accAbsolute = new Pair<>(states, transformationValue);
        } else {
            accAbsolute.getFirst().add(transformation.getFirst().getX());
            accAbsolute.setSecond(accAbsolute.getSecond() + transformationValue);
        }

        if (accAbsolute.getSecond() >= threshold) {
            Event event = new Event(
                    Event.ThresholdTrigger.ABSOLUTE_ACC,
                    type,
                    accAbsolute.getSecond());
            accAbsolute.getFirst().add(transformation.getFirst().getY());
            event.setPhenomena(accAbsolute.getFirst());
            return event;
        }
        return null;
    }

    /**
     * Single processing of one threshold. The process is similar to all thresholds
     *
     * @param transformations The array to be processed, containing the the transformation values
     * @param threshold       The thresholds to use
     * @param type            The type of the transformation being analyzed
     */
    protected ArrayList<Event> filterThreshold(List<Pair<Snapshot, Float>> transformations, GenericThreshold<Float> threshold, Event.Transformation type) {
        ArrayList<Event> eventsOfInterest = new ArrayList<>();

        for (Pair<Snapshot, Float> transformation : transformations) {
            Event event = computeDelta(transformation, threshold.getDelta(), type);
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

    protected abstract ArrayList<Event> parse(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull GenericThreshold<Float> threshold);

    public static ArrayList<Event> parseTransformations(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull ThresholdParameters thresholds) {
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

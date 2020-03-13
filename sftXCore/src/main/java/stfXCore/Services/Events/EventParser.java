package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public abstract class EventParser {

    protected enum Direction {
        FORWARD,
        BACKWARD,
        NONE
    }

    protected Direction direction = Direction.NONE;
    // TODO: Pair<initialState, float>
    protected float accDirected = 0;
    protected float accAbsolute = 0;


    EventParser() {
    }

    private void resetDeltas() {
        direction = Direction.NONE;
        accDirected = 0;
        accAbsolute = 0;
    }

    protected Event computeDelta(Float transformation, Float threshold, Event.Transformation type) {
        if (transformation >= threshold)
            return new Event(Event.ThresholdTrigger.DELTA, type, transformation);
        return null;
    }

    protected Event computeAccDirected(Float transformation, Float threshold, Event.Transformation type) {
        // If direction is different from current, reset
        if (!((direction == Direction.FORWARD && transformation >= 0) ||
                (direction == Direction.BACKWARD && transformation < 0))) {
            direction = transformation > 0 ? Direction.FORWARD : Direction.BACKWARD;
            accDirected = 0;
        }

        accDirected += transformation;

        if (Math.abs(accDirected) >= threshold)
            return new Event(Event.ThresholdTrigger.DIRECTED_ACC,
                    type,
                    accDirected);
        return null;
    }

    protected Event computeAccAbsolute(Float transformation, Float threshold, Event.Transformation type) {
        accAbsolute += Math.abs(transformation);

        if (accAbsolute >= threshold)
            return new Event(Event.ThresholdTrigger.ABSOLUTE_ACC,
                    type,
                    accAbsolute);
        return null;
    }

    /**
     * Single processing of one threshold. The process is similar to all thresholds
     *
     * @param transformations The array to be processed, containing the the transformation values
     * @param threshold       The thresholds to use
     * @param type            The type of the transformation being analyzed
     */
    protected ArrayList<Event> filterThreshold(List<Float> transformations, GenericThreshold<Float> threshold, Event.Transformation type) {
        ArrayList<Event> eventsOfInterest = new ArrayList<>();

        for (Float transformation : transformations) {
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

        //TODO: Apply reorder to map to each time unit the operation ocurring

        return eventsOfInterest;
    }
}

package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventsFilter {

    // TODO: Pair<initialState, float>
    private float accDirected = 0;
    // TODO:  as enum -> 0 for no direction, 1 for positive, -1 for negative
    private int direction = 0;
    private float accAbsolute = 0;

    EventsFilter() {
    }

    private void resetDeltas() {
        accDirected = 0;
        direction = 0;
        accAbsolute = 0;
    }

    private Event computeDelta(Float transformation, Float threshold, Event.Transformation type) {
        if (transformation >= threshold)
            return new Event(Event.ThresholdTrigger.DELTA, type, transformation);
        return null;
    }

    private Event computeAccDirected(Float transformation, Float threshold, Event.Transformation type) {
        // TODO: Change with enum
        // If direction are different from current, reset
        if (!((direction == 1 && transformation >= 0) ||
                (direction == -1 && transformation < 0))) {
            direction = transformation >= 0 ? 1 : -1;
            accDirected = 0;
        }

        accDirected += Math.abs(transformation);

        if (accDirected >= threshold) {
            accDirected = 0;
            return new Event(Event.ThresholdTrigger.DIRECTED_ACC,
                    type,
                    accDirected);
        }
        return null;
    }

    private Event computeAccAbsolute(Float transformation, Float threshold, Event.Transformation type) {
        accAbsolute += Math.abs(transformation);

        if (accAbsolute >= threshold) {
            accAbsolute = 0;
            return new Event(Event.ThresholdTrigger.ABSOLUTE_ACC,
                    type,
                    accAbsolute);
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
    private ArrayList<Event> filterThreshold(List<Float> transformations, GenericThreshold<Float> threshold, Event.Transformation type) {
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

    public static ArrayList<Event> filter(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull Thresholds thresholds) {
        EventsFilter ev = new EventsFilter();

        // Calls the filter threshold function
        ArrayList<Event> eventsOfInterest = new ArrayList<>(ev.filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getRotation()).collect(Collectors.toList()),
                thresholds.getRotation(),
                Event.Transformation.ROTATION));

        eventsOfInterest.addAll(ev.filterThreshold(
                rigidTransformations.stream().map(
                        rt -> {
                            ArrayList<Float> translation = rt.getTranslation();
                            return (float) Math.sqrt(translation.stream().reduce(
                                    0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
                        }).collect(Collectors.toList()),
                thresholds.getTranslation(),
                Event.Transformation.TRANSLATION));

        eventsOfInterest.addAll(ev.filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getScale()).collect(Collectors.toList()),
                thresholds.getScale(),
                Event.Transformation.UNIFORM_SCALE));
        // What is the return type?

        //TODO: Apply reorder to map to each time unit the operation ocurring

        return eventsOfInterest;
    }
}

package stfXCore.Services;

import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventsFilter {

    /**
     * Single processing of one threshold. The process is similar to all thresholds
     *
     * @param transformations The array to be processed, containing the the transformation values
     * @param threshold       the threshold to use
     */
    // TODO: Refactor into several functions
    private static ArrayList<Event> filterThreshold(List<Float> transformations, GenericThreshold<Float> threshold, Event.Transformation type) {
        ArrayList<Event> eventsOfInterest = new ArrayList<>();

        // TODO: Pair<initialState, float>
        float accDirected = 0;
        // TODO:  as enum -> 0 for no direction, 1 for positive, -1 for negative
        int direction = 0;
        float accAbsolute = 0;

        //TODO: How to manage simultaneous thresholds of a transformation?
        for (Float transformation: transformations) {
            if (transformation >= threshold.getDelta()) {
                eventsOfInterest.add(
                        new Event(Event.ThresholdTrigger.DELTA,
                                type,
                                transformation));
            }

            // TODO: Change with enum
            // If direction are different from current, reset
            if (threshold.getDirectedAcc() != null) {
                if (!((direction == 1 && transformation >= 0) ||
                        (direction == -1 && transformation < 0))) {
                    direction = transformation >= 0 ? 1 : -1;
                    accDirected = 0;
                }

                accDirected += Math.abs(transformation);

                if (accDirected >= threshold.getDirectedAcc()) {
                    eventsOfInterest.add(
                            new Event(Event.ThresholdTrigger.DIRECTED_ACC,
                                    type,
                                    accDirected));
                    accDirected = 0;
                }
            }

            if (threshold.getAbsoluteAcc() != null) {
                accAbsolute += Math.abs(transformation);
                if (accAbsolute >= threshold.getAbsoluteAcc()) {
                    eventsOfInterest.add(
                            new Event(Event.ThresholdTrigger.ABSOLUTE_ACC,
                                    type,
                                    accAbsolute));
                    accAbsolute = 0;
                }
            }
        }

        return eventsOfInterest;
    }

    public static ArrayList<Event> filter(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull Thresholds thresholds) {

        // Calls the filter threshold function
        ArrayList<Event> eventsOfInterest = new ArrayList<>(filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getRotation()).collect(Collectors.toList()),
                thresholds.getRotation(),
                Event.Transformation.ROTATION));

        eventsOfInterest.addAll(filterThreshold(
                rigidTransformations.stream().map(
                        rt -> {
                            ArrayList<Float> translation = rt.getTranslation();
                            return (float) Math.sqrt(translation.stream().reduce(
                                            0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
                        }).collect(Collectors.toList()),
                thresholds.getTranslation(),
                Event.Transformation.TRANSLATION));

        eventsOfInterest.addAll(filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getScale()).collect(Collectors.toList()),
                thresholds.getScale(),
                Event.Transformation.UNIFORM_SCALE));
        // What is the return type?

        //TODO: Apply reorder to map to each time unit the operation ocurring

        return eventsOfInterest;
    }
}

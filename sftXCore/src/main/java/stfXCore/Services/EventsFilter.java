package stfXCore.Services;

import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;

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
    private static ArrayList<Event> filterThreshold(List<Float> transformations, GenericThreshold<Float> threshold) {
        ArrayList<Event> eventsOfInterest = new ArrayList<>();

        // TODO: Pair<initialState, float>
        float accDirected = 0;
        // TODO:  as enum -> 0 for no direction, 1 for positive, -1 for negative
        int direction = 0;
        float accAbsolute = 0;

        //TODO: How to manage simultaneous thresholds of a transformation?
        for (Float transformation : transformations) {
            if (transformation >= threshold.getDelta()) {
                eventsOfInterest.add(
                        new Event(Event.ThresholdTrigger.DELTA,
                                Event.Transformation.TRANSLATION,
                                transformation));
            }

            // TODO: Change with enum
            // If direction are different from current, reset
            if (!((direction == 1 && transformation >= 0) ||
                    (direction == -1 && transformation < 0))) {
                direction = transformation >= 0? 1 : -1;
                accDirected = 0;
            }

            accDirected += Math.abs(transformation);

            if (accDirected >= threshold.getDirectedAcc()) {
                eventsOfInterest.add(
                        new Event(Event.ThresholdTrigger.DIRECTED_ACC,
                                Event.Transformation.TRANSLATION,
                                accDirected));
                accDirected = 0;
            }

            accAbsolute += Math.abs(transformation);
            if (accAbsolute >= threshold.getAbsoluteAcc()) {
                eventsOfInterest.add(
                        new Event(Event.ThresholdTrigger.ABSOLUTE_ACC,
                                Event.Transformation.TRANSLATION,
                                accAbsolute));
                accAbsolute = 0;
            }
        }

        return eventsOfInterest;
    }

    public static void filter(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull Thresholds thresholds) {

        // Calls the filter threshold function
        filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getRotation()).collect(Collectors.toList()),
                thresholds.getRotation());
//        filterThreshold(
//                rigidTransformations.stream().map(rt -> rt.getTranslation()[0]).collect(Collectors.toList()),
//                thresholds.getRotation()[0]);


        // What is the return type?

    }
}

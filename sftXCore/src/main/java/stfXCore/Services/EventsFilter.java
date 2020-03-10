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
    private static void filterThreshold(List<Float> transformations, GenericThreshold<Float> threshold) {
        // TODO: X and Y são processados individualmente
        System.out.print(transformations);
        System.out.print(threshold);
    }

    public static void filter(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull Thresholds thresholds) {

        // Calls the filter threshold function
        filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getRotation()).collect(Collectors.toList()),
                thresholds.getRotation());
        filterThreshold(
                rigidTransformations.stream().map(rt -> rt.getTranslation()).collect(Collectors.toList()),
                thresholds.getRotation());


        // What is the return type?

    }
}

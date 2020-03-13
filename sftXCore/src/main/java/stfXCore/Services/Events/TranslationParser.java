package stfXCore.Services.Events;

import stfXCore.Models.Storyboard.Thresholds.GenericThreshold;
import stfXCore.Models.Storyboard.Thresholds.ThresholdParameters;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class TranslationParser extends EventParser {

    TranslationParser() {
    }

    public ArrayList<Event> parse(@NotNull ArrayList<RigidTransformation> rigidTransformations, @NotNull GenericThreshold<Float> threshold) {
        return new ArrayList<>(filterThreshold(
                rigidTransformations.stream().map(
                        rt -> {
                            ArrayList<Float> translation = rt.getTranslation();
                            // Using Magnitude for evaluating translation
                            return (float) Math.sqrt(translation.stream().reduce(
                                    0f, (acc, el) -> acc + (float) Math.pow(el, 2)));
                        }).collect(Collectors.toList()),
                threshold,
                Event.Transformation.TRANSLATION));
    }
}

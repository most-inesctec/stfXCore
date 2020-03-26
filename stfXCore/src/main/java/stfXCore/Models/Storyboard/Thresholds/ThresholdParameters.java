package stfXCore.Models.Storyboard.Thresholds;

import lombok.Data;

@Data
public class ThresholdParameters {

    private TranslationThresholds translation;

    private RotationThresholds rotation;

    private UniformScaleThresholds scale;

    public ThresholdParameters() {}
}

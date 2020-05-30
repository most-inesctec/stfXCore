package stfXCore.Models.Thresholds;

import lombok.Data;

@Data
public class ThresholdParameters {

    private TranslationThresholds translation;

    private RotationThresholds rotation;

    private UniformScaleThresholds scale;

    private Long immutability;

    public ThresholdParameters() {}
}

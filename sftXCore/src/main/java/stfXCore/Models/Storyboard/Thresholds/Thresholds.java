package stfXCore.Models.Storyboard.Thresholds;

import lombok.Data;

@Data
public class Thresholds {

    private TranslationThresholds translation;

    private RotationThresholds rotation;

    private UniformScaleThresholds scale;

    Thresholds() {}
}

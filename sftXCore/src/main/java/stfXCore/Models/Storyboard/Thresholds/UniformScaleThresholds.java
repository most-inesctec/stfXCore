package stfXCore.Models.Storyboard.Thresholds;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UniformScaleThresholds {

    private ArrayList<Float> delta;

    private ArrayList<Float> acc;

    UniformScaleThresholds() {}
}

package stfXCore.Models.Storyboard.Thresholds;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TranslationThresholds {

    private ArrayList<Float> delta;

    private ArrayList<Float> directedAcc;

    private ArrayList<Float> absoluteAcc;

    TranslationThresholds() {}
}

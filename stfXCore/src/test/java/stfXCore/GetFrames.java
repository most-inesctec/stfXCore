package stfXCore;

import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Frames.CoalescedFramedDataset;
import stfXCore.Services.Frames.Frame;
import stfXCore.Services.Frames.FramedDataset;
import stfXCore.Services.Frames.FramedDatasetWithUnimportantFrames;

import java.util.ArrayList;

public abstract class GetFrames {

    protected ArrayList<Frame> getFrames(Storyboard storyboard, Thresholds thresholds, Long lb, Long up) {
        return new FramedDatasetWithUnimportantFrames(
                new FramedDataset(storyboard, thresholds)
                        .restrictInterval(lb, up))
                .getFrames();
    }

    protected ArrayList<Frame> getCoalescedFrames(Storyboard storyboard, Thresholds thresholds, Long lb, Long up) {
        return new CoalescedFramedDataset(
                new FramedDatasetWithUnimportantFrames(
                        new FramedDataset(storyboard, thresholds)
                                .restrictInterval(lb, up)))
                .getFrames();
    }
}

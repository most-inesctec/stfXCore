package stfXCore;

import org.junit.jupiter.api.Assertions;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.DataTypes.NullTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Frames.CoalescedFramedDataset;
import stfXCore.Services.Frames.Frame;
import stfXCore.Services.Frames.FramedDataset;
import stfXCore.Services.Frames.FramedDatasetWithUnimportantFrames;

import java.util.ArrayList;

public abstract class FramesAnalyser {

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

    protected void checkUnimportant(Frame frame, Long[] interval) {
        Assertions.assertArrayEquals(frame.getTemporalRange().toArray(new Long[frame.getTemporalRange().size()]), interval);
        Assertions.assertEquals(frame.getEvents().size(), 1);
        EventDataWithTrigger<NullTransformation> eventAF = frame.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), null);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.UNIMPORTANT);
        Assertions.assertEquals(eventAF.getTrigger(), null);
    }
}

package stfXCore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stfXCore.Models.Snapshot;
import stfXCore.Models.Storyboard;
import stfXCore.Models.Thresholds.*;
import stfXCore.Models.Transformations.RigidTransformation;
import stfXCore.Models.Transformations.SnapshotTransformationPair;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Frames.Frame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoiseFilteringTests extends FramesAnalyser {

    Storyboard storyboard;

    Thresholds thresholds;

    private void mockData() {
        RigidTransformation noise = new RigidTransformation();
        noise.setScale(1.001f);
        noise.setTranslation(new ArrayList<>(Arrays.asList(0.01f, 0f)));
        noise.setRotation(0.01f);

        RigidTransformation noiseAllButRotation = new RigidTransformation();
        noiseAllButRotation.setScale(0.999f);
        noiseAllButRotation.setTranslation(new ArrayList<>(Arrays.asList(-0.01f, 0f)));
        noiseAllButRotation.setRotation(2f);


        List<SnapshotTransformationPair> data = new ArrayList<>(Arrays.asList(
                new SnapshotTransformationPair(new Snapshot().setX(null, 0L).setY(null, 1L), noise),
                new SnapshotTransformationPair(new Snapshot().setX(null, 1L).setY(null, 2L), noise),
                new SnapshotTransformationPair(new Snapshot().setX(null, 2L).setY(null, 3L), noise),
                new SnapshotTransformationPair(new Snapshot().setX(null, 3L).setY(null, 4L), noise),
                new SnapshotTransformationPair(new Snapshot().setX(null, 4L).setY(null, 5L), noiseAllButRotation),
                new SnapshotTransformationPair(new Snapshot().setX(null, 5L).setY(null, 6L), noiseAllButRotation)
        ));

        storyboard = new Storyboard();
        storyboard.setRigidTransformations(data);
    }

    private void mockThresholds1() {
        //Building Thresholds
        TranslationThresholds tThreshold = new TranslationThresholds();
        tThreshold.setDelta(1f);
        tThreshold.setDirectedAcc(0.02f);
        tThreshold.setAbsoluteAcc(0.02f);
        tThreshold.setNoiseEpsilon(0.02f);

        RotationThresholds rThreshold = new RotationThresholds();
        rThreshold.setDelta(1f);
        rThreshold.setDirectedAcc(0.02f);
        rThreshold.setAbsoluteAcc(0.02f);
        rThreshold.setNoiseEpsilon(0.02f);

        UniformScaleThresholds sThreshold = new UniformScaleThresholds();
        sThreshold.setDelta(1.1f);
        sThreshold.setDirectedAcc(1.1f);
        sThreshold.setAbsoluteAcc(2f);
        sThreshold.setNoiseEpsilon(0.02f);

        ThresholdParameters parameters = new ThresholdParameters();
        parameters.setTranslation(tThreshold);
        parameters.setRotation(rThreshold);
        parameters.setScale(sThreshold);

        thresholds = new Thresholds();
        thresholds.setParameters(parameters);
    }

    private void mockThresholds2() {
        //Building Thresholds
        TranslationThresholds tThreshold = new TranslationThresholds();
        tThreshold.setDelta(2f);
        tThreshold.setDirectedAcc(0.02f);
        tThreshold.setAbsoluteAcc(0.02f);
        tThreshold.setNoiseEpsilon(0.00002f);

        RotationThresholds rThreshold = new RotationThresholds();
        rThreshold.setDelta(1.8f);
        rThreshold.setDirectedAcc(0.02f);
        rThreshold.setAbsoluteAcc(0.02f);
        rThreshold.setNoiseEpsilon(0.00002f);

        UniformScaleThresholds sThreshold = new UniformScaleThresholds();
        sThreshold.setDelta(1.12f);
        sThreshold.setDirectedAcc(1.1f);
        sThreshold.setAbsoluteAcc(2f);
        sThreshold.setNoiseEpsilon(0.00002f);

        ThresholdParameters parameters = new ThresholdParameters();
        parameters.setTranslation(tThreshold);
        parameters.setRotation(rThreshold);
        parameters.setScale(sThreshold);

        thresholds = new Thresholds();
        thresholds.setParameters(parameters);
    }

    @Test
    public void checkNoiseFromEpsilon() {
        mockData();
        mockThresholds1();
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(frames.size(), 3);

        // Verify retrivied frames
        checkUnimportant(frames.get(0), new Long[]{0L, 4L});

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{4L, 5L});
        Assertions.assertEquals(frame2.getEvents().size(), 1);
        EventDataWithTrigger<FloatTransformation> eventF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DELTA);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 2f);

        Frame frame3 = frames.get(2);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{5L, 6L});
        Assertions.assertEquals(frame3.getEvents().size(), 1);
        eventF = frame3.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DELTA);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 2f);
    }

    @Test
    public void checkNoiseFromDelta() {
        mockData();
        mockThresholds2();
        ArrayList<Frame> frames = getCoalescedFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(frames.size(), 2);

        // Verify retrivied frames
        checkUnimportant(frames.get(0), new Long[]{0L, 4L});

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{4L, 6L});
        Assertions.assertEquals(frame2.getEvents().size(), 1);
        EventDataWithTrigger<FloatTransformation> eventF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DELTA);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 4f);
    }
}

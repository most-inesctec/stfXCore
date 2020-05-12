package stfXCore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.*;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Models.Storyboard.Transformations.SnapshotTransformationPair;
import stfXCore.Services.DataTypes.ArrayFloatTransformation;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Services.DataTypes.NullTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Frames.Frame;
import stfXCore.Utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetAffineFramesTests extends FramesAnalyser {

    Storyboard storyboard;

    Thresholds thresholds;

    // TODO change to be Affine transformations rather than this ones
    private void mockData() {
        RigidTransformation shear = new RigidTransformation();
        shear.setScale(1f);
        shear.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        shear.setRotation(0f);

        RigidTransformation skew = new RigidTransformation();
        skew.setScale(1f);
        skew.setTranslation(new ArrayList<>(Arrays.asList(0f, 0f)));
        skew.setRotation(1f);

        RigidTransformation skewAndShear = new RigidTransformation();
        skewAndShear.setScale(1f);
        skewAndShear.setTranslation(new ArrayList<>(Arrays.asList(1.5f, 1.5f)));
        skewAndShear.setRotation(-1f);

        RigidTransformation unimportant = new RigidTransformation();
        unimportant.setScale(1.1f);
        unimportant.setTranslation(new ArrayList<>(Arrays.asList(0f, 0f)));
        unimportant.setRotation(0f);

        List<SnapshotTransformationPair> data = new ArrayList<>(Arrays.asList(
                new SnapshotTransformationPair(new Snapshot().setX(null, 0L).setY(null, 1L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 1L).setY(null, 2L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 2L).setY(null, 3L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 3L).setY(null, 4L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 4L).setY(null, 5L), shear),
                new SnapshotTransformationPair(new Snapshot().setX(null, 5L).setY(null, 6L), shear),
                new SnapshotTransformationPair(new Snapshot().setX(null, 6L).setY(null, 7L), shear),
                new SnapshotTransformationPair(new Snapshot().setX(null, 7L).setY(null, 8L), shear),
                new SnapshotTransformationPair(new Snapshot().setX(null, 8L).setY(null, 9L), shear),
                new SnapshotTransformationPair(new Snapshot().setX(null, 9L).setY(null, 10L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 10L).setY(null, 11L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 11L).setY(null, 12L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 12L).setY(null, 13L), skew),
                new SnapshotTransformationPair(new Snapshot().setX(null, 13L).setY(null, 14L), skew),
                new SnapshotTransformationPair(new Snapshot().setX(null, 14L).setY(null, 15L), skew),
                new SnapshotTransformationPair(new Snapshot().setX(null, 15L).setY(null, 16L), skew),
                new SnapshotTransformationPair(new Snapshot().setX(null, 16L).setY(null, 17L), skew),
                new SnapshotTransformationPair(new Snapshot().setX(null, 17L).setY(null, 18L), skewAndShear),
                new SnapshotTransformationPair(new Snapshot().setX(null, 18L).setY(null, 19L), unimportant),
                new SnapshotTransformationPair(new Snapshot().setX(null, 19L).setY(null, 20L), unimportant)
        ));

        storyboard = new Storyboard();
        storyboard.setRigidTransformations(data);

        //Building Thresholds
        TranslationThresholds shearThreshold = new TranslationThresholds();
        shearThreshold.setDelta(2f);
        shearThreshold.setDirectedAcc(4.5f);
        shearThreshold.setAbsoluteAcc(10f);

        RotationThresholds skewThreshold = new RotationThresholds();
        skewThreshold.setDelta(2f);
        skewThreshold.setAbsoluteAcc(5.5f);

        UniformScaleThresholds sThreshold = new UniformScaleThresholds();
        sThreshold.setDelta(0.2f);
        sThreshold.setDirectedAcc(5f);
        sThreshold.setAbsoluteAcc(6f);

        ThresholdParameters parameters = new ThresholdParameters();
        parameters.setTranslation(shearThreshold);
        parameters.setRotation(skewThreshold);
        parameters.setScale(sThreshold);

        thresholds = new Thresholds();
        thresholds.setParameters(parameters);
    }

    @Test
    void verifyFramesParser() {
        mockData();
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(frames.size(), 6);

        // Verify retrivied frames
        checkUnimportant(frames.get(0), new Long[]{0L, 4L});

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{4L, 9L});
        Assertions.assertEquals(frame2.getEvents().size(), 1);
        EventDataWithTrigger<ArrayFloatTransformation> eventAF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 5f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);

        checkUnimportant(frames.get(2), new Long[]{9L, 12L});

        Frame frame4 = frames.get(3);
        Assertions.assertArrayEquals(frame4.getTemporalRange().toArray(new Long[frame4.getTemporalRange().size()]), new Long[]{12L, 17L});
        Assertions.assertEquals(frame4.getEvents().size(), 1);
        EventDataWithTrigger<FloatTransformation> eventF = frame4.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 5f);

        Frame frame5 = frames.get(4);
        Assertions.assertArrayEquals(frame5.getTemporalRange().toArray(new Long[frame5.getTemporalRange().size()]), new Long[]{17L, 18L});
        Assertions.assertEquals(frame5.getEvents().size(), 2);
        eventF = frame5.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), -1f);
        eventAF = frame5.getEvents().get(1);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DELTA);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 1.5f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 1.5f);

        checkUnimportant(frames.get(5), new Long[]{18L, 20L});
    }

    @Test
    void verifySingleUnimportant() {
        mockData();
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, 18L, 20L);
        Assertions.assertEquals(frames.size(), 1);

        // Verify retrivied frames
        checkUnimportant(frames.get(0), new Long[]{18L, 20L});
    }
}

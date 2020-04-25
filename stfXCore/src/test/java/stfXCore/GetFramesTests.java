package stfXCore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.*;
import stfXCore.Services.DataTypes.NullTransformation;
import stfXCore.Services.DataTypes.ScaleFloatTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Events.UnimportantEvent;
import stfXCore.Services.Frames.Frame;
import stfXCore.Services.DataTypes.ArrayFloatTransformation;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Services.Frames.FramedDataset;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class GetFramesTests extends GetFrames {

    Storyboard storyboard;

    Thresholds thresholds;

    private void mockData() {
        RigidTransformation translation = new RigidTransformation();
        translation.setScale(1f);
        translation.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        translation.setRotation(0f);

        RigidTransformation rotation = new RigidTransformation();
        rotation.setScale(1f);
        rotation.setTranslation(new ArrayList<>(Arrays.asList(0f, 0f)));
        rotation.setRotation(1f);

        RigidTransformation translationAndRotation = new RigidTransformation();
        translationAndRotation.setScale(1f);
        translationAndRotation.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        translationAndRotation.setRotation(1f);

        RigidTransformation rotationAndScale = new RigidTransformation();
        rotationAndScale.setScale(1.1f);
        rotationAndScale.setTranslation(new ArrayList<>(Arrays.asList(0f, 0f)));
        rotationAndScale.setRotation(-1f);

        RigidTransformation translationAndScaleAndRotation = new RigidTransformation();
        translationAndScaleAndRotation.setScale(1.1f);
        translationAndScaleAndRotation.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        translationAndScaleAndRotation.setRotation(1f);

        ArrayList<Pair<Snapshot, RigidTransformation>> data = new ArrayList<>(Arrays.asList(
                new Pair<>(new Snapshot().setX(null, 0L).setY(null, 1L), translation),
                new Pair<>(new Snapshot().setX(null, 1L).setY(null, 2L), translation),
                new Pair<>(new Snapshot().setX(null, 2L).setY(null, 3L), translation),
                new Pair<>(new Snapshot().setX(null, 3L).setY(null, 4L), translation),
                new Pair<>(new Snapshot().setX(null, 4L).setY(null, 5L), translation),
                new Pair<>(new Snapshot().setX(null, 5L).setY(null, 6L), translationAndRotation),
                new Pair<>(new Snapshot().setX(null, 6L).setY(null, 7L), translationAndRotation),
                new Pair<>(new Snapshot().setX(null, 7L).setY(null, 8L), translationAndScaleAndRotation),
                new Pair<>(new Snapshot().setX(null, 8L).setY(null, 9L), translationAndScaleAndRotation),
                new Pair<>(new Snapshot().setX(null, 9L).setY(null, 10L), translationAndScaleAndRotation),
                new Pair<>(new Snapshot().setX(null, 10L).setY(null, 11L), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 11L).setY(null, 12L), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 12L).setY(null, 13L), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 13L).setY(null, 14L), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 14L).setY(null, 15L), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 15L).setY(null, 16L), rotation),
                new Pair<>(new Snapshot().setX(null, 16L).setY(null, 17L), rotation),
                new Pair<>(new Snapshot().setX(null, 17L).setY(null, 18L), rotation),
                new Pair<>(new Snapshot().setX(null, 18L).setY(null, 19L), rotation),
                new Pair<>(new Snapshot().setX(null, 19L).setY(null, 20L), rotation)
        ));

        storyboard = new Storyboard();
        storyboard.setRigidTransformations(data);

        //Building Thresholds
        TranslationThresholds tThreshold = new TranslationThresholds();
        tThreshold.setDelta(2f);
        tThreshold.setDirectedAcc(10f);
        tThreshold.setAbsoluteAcc(12f);

        RotationThresholds rThreshold = new RotationThresholds();
        rThreshold.setDelta(2f);
        rThreshold.setDirectedAcc(8f);
        rThreshold.setAbsoluteAcc(15f);

        UniformScaleThresholds sThreshold = new UniformScaleThresholds();
        sThreshold.setDelta(0.2f);
        sThreshold.setDirectedAcc(1.1f);
        sThreshold.setAbsoluteAcc(2f);

        ThresholdParameters parameters = new ThresholdParameters();
        parameters.setTranslation(tThreshold);
        parameters.setRotation(rThreshold);
        parameters.setScale(sThreshold);

        thresholds = new Thresholds();
        thresholds.setParameters(parameters);
    }

    @Test
    void verifyFramesParser() {
        mockData();
        ArrayList<Frame> frames = new FramedDataset(storyboard, thresholds).getFrames();
        Assertions.assertEquals(frames.size(), 5);

        // Verify retrivied frames
        Frame frame1 = frames.get(0);
        Assertions.assertArrayEquals(frame1.getTemporalRange().toArray(new Long[frame1.getTemporalRange().size()]), new Long[]{0L, 5L});
        Assertions.assertEquals(frame1.getEvents().size(), 1);
        EventDataWithTrigger<ArrayFloatTransformation> eventAF = frame1.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(0), 5f);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(1), 0f);

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{5L, 7L});
        Assertions.assertEquals(frame2.getEvents().size(), 2);
        eventAF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(1), 0f);
        EventDataWithTrigger<FloatTransformation> eventF = frame2.getEvents().get(1);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getValue(), 2f);

        Frame frame3 = frames.get(2);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{7L, 10L});
        Assertions.assertEquals(frame3.getEvents().size(), 3);
        eventAF = frame3.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(0), 3f);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(1), 0f);
        eventF = frame3.getEvents().get(1);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getValue(), 3f);
        EventDataWithTrigger<ScaleFloatTransformation> eventSF = frame3.getEvents().get(2);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getValue() * 100) / 100f, 1.33f);

        Frame frame4 = frames.get(3);
        Assertions.assertArrayEquals(frame4.getTemporalRange().toArray(new Long[frame4.getTemporalRange().size()]), new Long[]{10L, 15L});
        Assertions.assertEquals(frame4.getEvents().size(), 2);
        eventF = frame4.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getValue(), -5f);
        eventSF = frame4.getEvents().get(1);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getValue() * 100) / 100f, 1.61f);

        Frame frame5 = frames.get(4);
        Assertions.assertArrayEquals(frame5.getTemporalRange().toArray(new Long[frame5.getTemporalRange().size()]), new Long[]{15L, 20L});
        Assertions.assertEquals(frame5.getEvents().size(), 1);
        eventF = frame5.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getValue(), 5f);
    }

    @Test
    void initialTimestampFramesParser() {
        mockData();
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, 5L, null);
        Assertions.assertEquals(frames.size(), 3);

        // Verify retrivied frames
        Frame frame2 = frames.get(0);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{5L, 7L});
        Assertions.assertEquals(frame2.getEvents().size(), 1);
        EventDataWithTrigger<FloatTransformation> eventF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getValue(), 2f);

        Frame frame3 = frames.get(1);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{7L, 15L});
        Assertions.assertEquals(frame3.getEvents().size(), 2);
        eventF = frame3.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getValue(), -2f);
        EventDataWithTrigger<ScaleFloatTransformation> eventSF = frame3.getEvents().get(1);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getValue() * 100) / 100f, 2.14f);

        Frame frame5 = frames.get(2);
        Assertions.assertArrayEquals(frame5.getTemporalRange().toArray(new Long[frame5.getTemporalRange().size()]), new Long[]{15L, 20L});
        Assertions.assertEquals(frame5.getEvents().size(), 1);
        eventF = frame5.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getValue(), 5f);
    }

    @Test
    void finalTimestampAndUnimportantFramesParser() {
        mockData();
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, null, 18L);
        Assertions.assertEquals(frames.size(), 4);

        // Verify retrivied frames
        Frame frame1 = frames.get(0);
        Assertions.assertArrayEquals(frame1.getTemporalRange().toArray(new Long[frame1.getTemporalRange().size()]), new Long[]{0L, 7L});
        Assertions.assertEquals(frame1.getEvents().size(), 1);
        EventDataWithTrigger<ArrayFloatTransformation> eventAF = frame1.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(0), 7f);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(1), 0f);

        Frame frame3 = frames.get(1);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{7L, 10L});
        Assertions.assertEquals(frame3.getEvents().size(), 2);
        eventAF = frame3.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(0), 3f);
        Assertions.assertEquals(eventAF.getTrigger().getValue().get(1), 0f);
        EventDataWithTrigger<ScaleFloatTransformation> eventSF = frame3.getEvents().get(1);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getValue() * 100) / 100f, 1.33f);

        Frame frame4 = frames.get(2);
        Assertions.assertArrayEquals(frame4.getTemporalRange().toArray(new Long[frame4.getTemporalRange().size()]), new Long[]{10L, 15L});
        Assertions.assertEquals(frame4.getEvents().size(), 1);
        eventSF = frame4.getEvents().get(0);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getValue() * 100) / 100f, 1.61f);

        Frame frame5 = frames.get(3);
        Assertions.assertArrayEquals(frame5.getTemporalRange().toArray(new Long[frame5.getTemporalRange().size()]), new Long[]{15L, 18L});
        Assertions.assertEquals(frame5.getEvents().size(), 1);
        EventDataWithTrigger<NullTransformation> eventU = frame5.getEvents().get(0);
        Assertions.assertEquals(eventU.getThreshold(), null);
        Assertions.assertEquals(eventU.getType(), Event.Transformation.UNIMPORTANT);
        Assertions.assertEquals(eventU.getTrigger(), null);
    }

    @Test
    void initialAndFinalTimestampsAndUnimportantFramesParser() {
        mockData();
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, 5L, 18L);
        Assertions.assertEquals(frames.size(), 3);

        // Verify retrivied frames
        Frame frame1 = frames.get(0);
        Assertions.assertArrayEquals(frame1.getTemporalRange().toArray(new Long[frame1.getTemporalRange().size()]), new Long[]{5L, 7L});
        Assertions.assertEquals(frame1.getEvents().size(), 1);
        EventDataWithTrigger<NullTransformation> eventU = frame1.getEvents().get(0);
        Assertions.assertEquals(eventU.getThreshold(), null);
        Assertions.assertEquals(eventU.getType(), Event.Transformation.UNIMPORTANT);
        Assertions.assertEquals(eventU.getTrigger(), null);

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{7L, 15L});
        Assertions.assertEquals(frame2.getEvents().size(), 1);
        EventDataWithTrigger<ScaleFloatTransformation> eventSF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getValue() * 100) / 100f, 2.14f);

        Frame frame3 = frames.get(2);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{15L, 18L});
        Assertions.assertEquals(frame3.getEvents().size(), 1);
        eventU = frame3.getEvents().get(0);
        Assertions.assertEquals(eventU.getThreshold(), null);
        Assertions.assertEquals(eventU.getType(), Event.Transformation.UNIMPORTANT);
        Assertions.assertEquals(eventU.getTrigger(), null);
    }
}

package stfXCore;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stfXCore.Models.SnapshotPair;
import stfXCore.Models.Storyboard;
import stfXCore.Models.Thresholds.*;
import stfXCore.Models.Transformations.RigidTransformation;
import stfXCore.Models.Transformations.SnapshotTransformationPair;
import stfXCore.Services.DataTypes.ArrayFloatTransformation;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Services.DataTypes.ScaleFloatTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Frames.Frame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoalescingTests extends FramesAnalyser {

    Storyboard storyboard;

    Thresholds thresholds;

    private void mockThresholds() {
        //Building Thresholds
        TranslationThresholds tThreshold = new TranslationThresholds();
        tThreshold.setDelta(1.5f);
        tThreshold.setDirectedAcc(2f);
        tThreshold.setAbsoluteAcc(4f);

        RotationThresholds rThreshold = new RotationThresholds();
        rThreshold.setDelta(5f);
        rThreshold.setDirectedAcc(7f);
        rThreshold.setAbsoluteAcc(8f);

        UniformScaleThresholds sThreshold = new UniformScaleThresholds();
        sThreshold.setDelta(0.25f);
        sThreshold.setDirectedAcc(0.4f);
        sThreshold.setAbsoluteAcc(1f);

        ThresholdParameters parameters = new ThresholdParameters();
        parameters.setTranslation(tThreshold);
        parameters.setRotation(rThreshold);
        parameters.setScale(sThreshold);

        thresholds = new Thresholds();
        thresholds.setParameters(parameters);
    }

    @Test
    public void verifySimpleCoalescing() {
        mockThresholds();

        RigidTransformation translation = new RigidTransformation();
        translation.setScale(1f);
        translation.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        translation.setRotation(0f);

        List<SnapshotTransformationPair> data = new ArrayList<>(Arrays.asList(
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 0L).setY(null, 1L), translation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 1L).setY(null, 2L), translation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 2L).setY(null, 3L), translation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 3L).setY(null, 4L), translation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 4L).setY(null, 5L), translation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 5L).setY(null, 6L), translation)
        ));

        storyboard = new Storyboard();
        storyboard.setRigidTransformations(data);

        // Verify frames
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(3, frames.size());

        Frame frame1 = frames.get(0);
        Assertions.assertArrayEquals(frame1.getTemporalRange().toArray(new Long[frame1.getTemporalRange().size()]), new Long[]{0L, 2L});
        Assertions.assertEquals(frame1.getEvents().size(), 1);
        EventDataWithTrigger<ArrayFloatTransformation> eventAF = frame1.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{2L, 4L});
        Assertions.assertEquals(frame2.getEvents().size(), 1);
        eventAF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);

        Frame frame3 = frames.get(2);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{4L, 6L});
        Assertions.assertEquals(frame3.getEvents().size(), 1);
        eventAF = frame3.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);

        // Verify coalesced frames
        frames = getCoalescedFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(1, frames.size());

        Frame frame = frames.get(0);
        Assertions.assertArrayEquals(frame.getTemporalRange().toArray(new Long[frame.getTemporalRange().size()]), new Long[]{0L, 6L});
        Assertions.assertEquals(frame.getEvents().size(), 1);
        eventAF = frame.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 6f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
    }

    private EventDataWithTrigger getEventAccordingToType(Frame frame, Event.Transformation type) {
        ArrayList<EventDataWithTrigger> events = frame.getEvents();
        for (EventDataWithTrigger event : events) {
            if (event.getType() == type)
                return event;
        }
        return null;
    }

    @Test
    public void verifyComplexCoalescing() {
        mockThresholds();

        RigidTransformation translationAndScaleAndRotation = new RigidTransformation();
        translationAndScaleAndRotation.setScale(1.2f);
        translationAndScaleAndRotation.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        translationAndScaleAndRotation.setRotation(4f);

        RigidTransformation translationAndScaleAndAbsoluteRotationNegative = new RigidTransformation();
        translationAndScaleAndAbsoluteRotationNegative.setScale(1.2f);
        translationAndScaleAndAbsoluteRotationNegative.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        translationAndScaleAndAbsoluteRotationNegative.setRotation(-4f);

        RigidTransformation translationAndScaleAndAbsoluteRotationPositive = new RigidTransformation();
        translationAndScaleAndAbsoluteRotationPositive.setScale(1.2f);
        translationAndScaleAndAbsoluteRotationPositive.setTranslation(new ArrayList<>(Arrays.asList(1f, 0f)));
        translationAndScaleAndAbsoluteRotationPositive.setRotation(4f);

        List<SnapshotTransformationPair> data = new ArrayList<>(Arrays.asList(
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 0L).setY(null, 1L), translationAndScaleAndRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 1L).setY(null, 2L), translationAndScaleAndRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 2L).setY(null, 3L), translationAndScaleAndRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 3L).setY(null, 4L), translationAndScaleAndRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 4L).setY(null, 5L), translationAndScaleAndAbsoluteRotationNegative),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 5L).setY(null, 6L), translationAndScaleAndAbsoluteRotationPositive)
        ));

        storyboard = new Storyboard();
        storyboard.setRigidTransformations(data);

        // Verify frames
        ArrayList<Frame> frames = getFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(3, frames.size());

        Frame frame1 = frames.get(0);
        Assertions.assertArrayEquals(frame1.getTemporalRange().toArray(new Long[frame1.getTemporalRange().size()]), new Long[]{0L, 2L});
        Assertions.assertEquals(frame1.getEvents().size(), 3);
        EventDataWithTrigger<ArrayFloatTransformation> eventAF = getEventAccordingToType(frame1, Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
        EventDataWithTrigger<FloatTransformation> eventF = getEventAccordingToType(frame1, Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 8f);
        EventDataWithTrigger<ScaleFloatTransformation> eventSF = getEventAccordingToType(frame1, Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getTrigger().getTransformation(), 1.44f);

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{2L, 4L});
        Assertions.assertEquals(frame2.getEvents().size(), 3);
        eventAF = getEventAccordingToType(frame2, Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
        eventF = getEventAccordingToType(frame2, Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 8f);
        eventSF = getEventAccordingToType(frame2, Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getTrigger().getTransformation(), 1.44f);

        Frame frame3 = frames.get(2);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{4L, 6L});
        Assertions.assertEquals(frame3.getEvents().size(), 3);
        eventAF = getEventAccordingToType(frame3, Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
        eventF = getEventAccordingToType(frame3, Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 0f);
        eventSF = getEventAccordingToType(frame3, Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getTrigger().getTransformation(), 1.44f);

        // Verify coalesced frames
        frames = getCoalescedFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(2, frames.size());

        frame2 = frames.get(0);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{0L, 4L});
        Assertions.assertEquals(frame2.getEvents().size(), 3);
        eventAF = getEventAccordingToType(frame2, Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 4f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
        eventF = getEventAccordingToType(frame2, Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 16f);
        eventSF = getEventAccordingToType(frame2, Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getTrigger().getTransformation(), 2.0736f);

        frame3 = frames.get(1);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Long[frame3.getTemporalRange().size()]), new Long[]{4L, 6L});
        Assertions.assertEquals(frame3.getEvents().size(), 3);
        eventAF = getEventAccordingToType(frame3, Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
        eventF = getEventAccordingToType(frame3, Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 0f);
        eventSF = getEventAccordingToType(frame3, Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(eventSF.getTrigger().getTransformation(), 1.44f);
    }

    @Test
    public void verifyDirectedCoalescing() {
        mockThresholds();

        RigidTransformation positiveRotation = new RigidTransformation();
        positiveRotation.setScale(1f);
        positiveRotation.setTranslation(new ArrayList<>(Arrays.asList(0f, 0f)));
        positiveRotation.setRotation(4f);

        RigidTransformation negativeRotation = new RigidTransformation();
        negativeRotation.setScale(1f);
        negativeRotation.setTranslation(new ArrayList<>(Arrays.asList(0f, 0f)));
        negativeRotation.setRotation(-4f);

        List<SnapshotTransformationPair> data = new ArrayList<>(Arrays.asList(
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 0L).setY(null, 1L), positiveRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 1L).setY(null, 2L), positiveRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 2L).setY(null, 3L), positiveRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 3L).setY(null, 4L), positiveRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 4L).setY(null, 5L), negativeRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 5L).setY(null, 6L), negativeRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 6L).setY(null, 7L), negativeRotation),
                new SnapshotTransformationPair(new SnapshotPair().setX(null, 7L).setY(null, 8L), negativeRotation)
        ));

        storyboard = new Storyboard();
        storyboard.setRigidTransformations(data);

        // Verify frames
        ArrayList<Frame> frames = getCoalescedFrames(storyboard, thresholds, null, null);
        Assertions.assertEquals(2, frames.size());

        Frame frame1 = frames.get(0);
        Assertions.assertArrayEquals(frame1.getTemporalRange().toArray(new Long[frame1.getTemporalRange().size()]), new Long[]{0L, 4L});
        Assertions.assertEquals(frame1.getEvents().size(), 1);
        EventDataWithTrigger<FloatTransformation> eventF = frame1.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 16f);

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Long[frame2.getTemporalRange().size()]), new Long[]{4L, 8L});
        Assertions.assertEquals(frame2.getEvents().size(), 1);
        eventF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), -16f);
    }
}

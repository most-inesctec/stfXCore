package stfXCore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.*;
import stfXCore.Services.DataTypes.ScaleFloatTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Frames.Frame;
import stfXCore.Services.DataTypes.ArrayFloatTransformation;
import stfXCore.Services.DataTypes.FloatTransformation;
import stfXCore.Services.Frames.FramedDataset;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class GetFramesTests {

    @Test
    void verifyFramesParser() {
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
                new Pair<>(new Snapshot().setX(null, 0).setY(null, 1), translation),
                new Pair<>(new Snapshot().setX(null, 1).setY(null, 2), translation),
                new Pair<>(new Snapshot().setX(null, 2).setY(null, 3), translation),
                new Pair<>(new Snapshot().setX(null, 3).setY(null, 4), translation),
                new Pair<>(new Snapshot().setX(null, 4).setY(null, 5), translation),
                new Pair<>(new Snapshot().setX(null, 5).setY(null, 6), translationAndRotation),
                new Pair<>(new Snapshot().setX(null, 6).setY(null, 7), translationAndRotation),
                new Pair<>(new Snapshot().setX(null, 7).setY(null, 8), translationAndScaleAndRotation),
                new Pair<>(new Snapshot().setX(null, 8).setY(null, 9), translationAndScaleAndRotation),
                new Pair<>(new Snapshot().setX(null, 9).setY(null, 10), translationAndScaleAndRotation),
                new Pair<>(new Snapshot().setX(null, 10).setY(null, 11), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 11).setY(null, 12), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 12).setY(null, 13), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 13).setY(null, 14), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 14).setY(null, 15), rotationAndScale),
                new Pair<>(new Snapshot().setX(null, 15).setY(null, 16), rotation),
                new Pair<>(new Snapshot().setX(null, 16).setY(null, 17), rotation),
                new Pair<>(new Snapshot().setX(null, 17).setY(null, 18), rotation),
                new Pair<>(new Snapshot().setX(null, 18).setY(null, 19), rotation),
                new Pair<>(new Snapshot().setX(null, 19).setY(null, 20), rotation)
        ));

        Storyboard storyboard = new Storyboard();
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

        Thresholds thresholds = new Thresholds();
        thresholds.setParameters(parameters);

        ArrayList<Frame> frames = FramedDataset.getFrames(storyboard, thresholds);
        Assertions.assertEquals(frames.size(), 5);

        // Verify retrivied frames
        Frame frame1 = frames.get(0);
        Assertions.assertArrayEquals(frame1.getTemporalRange().toArray(new Float[frame1.getTemporalRange().size()]), new Float[]{0f, 5f});
        Assertions.assertEquals(frame1.getEvents().size(), 1);
        EventDataWithTrigger<ArrayFloatTransformation> eventAF = frame1.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 5f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);

        Frame frame2 = frames.get(1);
        Assertions.assertArrayEquals(frame2.getTemporalRange().toArray(new Float[frame2.getTemporalRange().size()]), new Float[]{5f, 7f});
        Assertions.assertEquals(frame2.getEvents().size(), 2);
        eventAF = frame2.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 2f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
        EventDataWithTrigger<FloatTransformation> eventF = frame2.getEvents().get(1);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 2f);

        Frame frame3 = frames.get(2);
        Assertions.assertArrayEquals(frame3.getTemporalRange().toArray(new Float[frame3.getTemporalRange().size()]), new Float[]{7f, 10f});
        Assertions.assertEquals(frame3.getEvents().size(), 3);
        eventAF = frame3.getEvents().get(0);
        Assertions.assertEquals(eventAF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventAF.getType(), Event.Transformation.TRANSLATION);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(0), 3f);
        Assertions.assertEquals(eventAF.getTrigger().getTransformation().get(1), 0f);
        eventF = frame3.getEvents().get(1);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 3f);
        EventDataWithTrigger<ScaleFloatTransformation> eventSF = frame3.getEvents().get(2);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getTransformation() * 100) / 100f, 1.33f);

        Frame frame4 = frames.get(3);
        Assertions.assertArrayEquals(frame4.getTemporalRange().toArray(new Float[frame4.getTemporalRange().size()]), new Float[]{10f, 15f});
        Assertions.assertEquals(frame4.getEvents().size(), 2);
        eventF = frame4.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), -5f);
        eventSF = frame4.getEvents().get(1);
        Assertions.assertEquals(eventSF.getThreshold(), Event.ThresholdTrigger.DIRECTED_ACC);
        Assertions.assertEquals(eventSF.getType(), Event.Transformation.UNIFORM_SCALE);
        Assertions.assertEquals(Math.round(eventSF.getTrigger().getTransformation() * 100) / 100f, 1.61f);


        Frame frame5 = frames.get(4);
        Assertions.assertArrayEquals(frame5.getTemporalRange().toArray(new Float[frame5.getTemporalRange().size()]), new Float[]{15f, 20f});
        Assertions.assertEquals(frame5.getEvents().size(), 1);
        eventF = frame5.getEvents().get(0);
        Assertions.assertEquals(eventF.getThreshold(), Event.ThresholdTrigger.ABSOLUTE_ACC);
        Assertions.assertEquals(eventF.getType(), Event.Transformation.ROTATION);
        Assertions.assertEquals(eventF.getTrigger().getTransformation(), 5f);
    }
}

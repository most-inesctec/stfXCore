package stfXCore.Services.TemporalFrames;

import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventParser;

import java.util.PriorityQueue;

public class FramedDataset {

    public static PriorityQueue<Frame> getFrames(Storyboard storyboard, Thresholds thresholds) {
        PriorityQueue<Event> orderedEvents = EventParser.parseTransformations(
                storyboard.getRigidTransformations(),
                thresholds.getParameters()
        );

        // TODO

        return null;
    }
}

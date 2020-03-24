package stfXCore.Services.TemporalFrames;

import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventParser;
import stfXCore.Services.Events.EventWrapper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class FramedDataset {

    public static PriorityQueue<Frame> getFrames(Storyboard storyboard, Thresholds thresholds) {
        ArrayList<Event> eventsOfInterest = EventParser.parseTransformations(
                storyboard.getRigidTransformations(),
                thresholds.getParameters()
        );

        // Priority Queue of start and end events
        PriorityQueue<EventWrapper> orderedEvents = new PriorityQueue<EventWrapper>(
                (e1, e2) -> e1.getTmestamp() - e2.getTimestamp());
        for (Event event: eventsOfInterest)
            orderedEvents.addAll(event.getWrappers());
        

        return null;
    }
}

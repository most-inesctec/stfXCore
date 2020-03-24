package stfXCore.Services.TemporalFrames;

import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventParser;
import stfXCore.Services.Events.EventWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

import static stfXCore.Services.Events.EventWrapper.EventType.START_WRAPPER;

public class FramedDataset {

    public static ArrayList<Frame> getFrames(Storyboard storyboard, Thresholds thresholds) {
        ArrayList<Event> eventsOfInterest = EventParser.parseTransformations(
                storyboard.getRigidTransformations(),
                thresholds.getParameters()
        );

        // Priority Queue of start and end events
        PriorityQueue<EventWrapper> orderedEvents = new PriorityQueue<EventWrapper>(
                (e1, e2) -> e1.getTimestamp() - e2.getTimestamp());
        for (Event event: eventsOfInterest)
            orderedEvents.addAll(event.getWrappers());

        ArrayList<Event> validEvents = new ArrayList<>();
        ArrayList<Frame> framedDataset = new ArrayList<>();

        while(!orderedEvents.isEmpty()) {
            EventWrapper eventWrapper = orderedEvents.poll();

            Frame f = new Frame(null);
            for (Event e: validEvents)
                f.addEvent(e);

            if (eventWrapper.getType() == START_WRAPPER)
                validEvents.add(eventWrapper.getEvent());
            else
                validEvents.remove(eventWrapper.getEvent());
        }

        return framedDataset;
    }
}

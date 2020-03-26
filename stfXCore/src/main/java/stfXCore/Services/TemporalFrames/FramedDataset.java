package stfXCore.Services.TemporalFrames;

import stfXCore.Models.Storyboard.StateList;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Events.EventParser;
import stfXCore.Services.Events.EventWrapper;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static stfXCore.Services.Events.EventWrapper.EventType.START_WRAPPER;

public class FramedDataset {

    public static ArrayList<Frame> getFrames(Storyboard storyboard, Thresholds thresholds) {
        ArrayList<Event> eventsOfInterest = EventParser.parseTransformations(
                storyboard.getRigidTransformations(),
                thresholds.getParameters()
        );

        // Priority Queue of start and end events
        PriorityQueue<EventWrapper> orderedEvents = new PriorityQueue<>(
                (e1, e2) -> Math.round(e1.getTimestamp() - e2.getTimestamp()));
        for (Event event : eventsOfInterest)
            orderedEvents.addAll(event.getWrappers());

        ArrayList<Event> validEvents = new ArrayList<>();
        ArrayList<Frame> framedDataset = new ArrayList<>();
        StateList states = storyboard.getStates();

        while (!orderedEvents.isEmpty()) {
            EventWrapper eventWrapper = orderedEvents.poll();

            if (eventWrapper.getType() == START_WRAPPER)
                validEvents.add(eventWrapper.getEvent());
            else
                validEvents.remove(eventWrapper.getEvent());

            // Polling all events with equal timestamp
            EventWrapper test = orderedEvents.peek();
            while (orderedEvents.peek() != null &&
                    orderedEvents.peek().getTimestamp().equals(eventWrapper.getTimestamp())) {
                eventWrapper = orderedEvents.poll();
                if (eventWrapper.getType() == START_WRAPPER)
                    validEvents.add(eventWrapper.getEvent());
                else
                    validEvents.remove(eventWrapper.getEvent());
            }

            Frame frame;
            if (!orderedEvents.isEmpty()) {
                frame = new Frame(states.getStates(
                        eventWrapper.getTimestamp(),
                        orderedEvents.peek().getTimestamp()));
            } else {
                frame = new Frame(states.getStates(
                        eventWrapper.getTimestamp()));
            }
            for (Event validEvent : validEvents)
                frame.addEvent(new EventDataWithTrigger(
                        validEvent.getData(),
                        // TODO Watch out to the null this function can return
                        validEvent.getTriggerValue(frame.upperBound())));

            framedDataset.add(frame);
        }

        return framedDataset;
    }
}

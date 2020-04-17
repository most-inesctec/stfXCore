package stfXCore.Services.Frames;

import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Parsers.TransformationsParser;
import stfXCore.Services.Events.EventWrapper;
import stfXCore.Services.StateList;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static stfXCore.Services.Events.EventWrapper.EventType.START_WRAPPER;

public class FramedDataset {

    public static ArrayList<Frame> getFrames(Storyboard storyboard, Thresholds thresholds) {
        ArrayList<Event<?>> eventsOfInterest = TransformationsParser.parseTransformations(
                storyboard.getRigidTransformations(),
                thresholds.getParameters()
        );

        // Priority Queue of start and end events
        PriorityQueue<EventWrapper> orderedEvents = new PriorityQueue<>(
                (e1, e2) -> Math.toIntExact(e1.getTimestamp() - e2.getTimestamp()));
        for (Event<?> event : eventsOfInterest)
            orderedEvents.addAll(event.getWrappers());

        ArrayList<Event<?>> validEvents = new ArrayList<>();
        ArrayList<Frame> framedDataset = new ArrayList<>();
        StateList states = storyboard.getStates();

        while (!orderedEvents.isEmpty()) {
            EventWrapper eventWrapper = orderedEvents.poll();
            if (eventWrapper.getType() == START_WRAPPER)
                validEvents.add(eventWrapper.getEvent());
            else
                validEvents.remove(eventWrapper.getEvent());

            if (validEvents.size() == 0)
                continue;

            // Polling all events with equal timestamp
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
            for (Event<?> validEvent : validEvents)
                frame.addEvent(new EventDataWithTrigger(
                        validEvent.getData(),
                        validEvent.getTriggerValue(frame.lowerBound(), frame.upperBound())));

            framedDataset.add(frame);
        }

        return framedDataset;
    }
}

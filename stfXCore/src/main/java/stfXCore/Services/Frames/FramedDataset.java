package stfXCore.Services.Frames;

import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Parsers.ParserFactory;
import stfXCore.Services.Parsers.TransformationsParser;
import stfXCore.Services.Events.EventWrapper;
import stfXCore.Services.StateList;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static stfXCore.Services.Events.EventWrapper.EventType.START_WRAPPER;

public class FramedDataset {

    private Storyboard storyboard;

    private Thresholds thresholds;

    public FramedDataset(Storyboard storyboard, Thresholds thresholds) {
        this.storyboard = storyboard;
        this.thresholds = thresholds;
    }

    public ArrayList<Frame> getFrames(Long initalTimestamp, Long finalTimestamp) {
        ArrayList<Event<?>> eventsOfInterest = new ParserFactory(
                storyboard.getRigidTransformations(),
                thresholds.getParameters())
                .restrictInterval(initalTimestamp, finalTimestamp)
                .parseTransformations();

        // Priority Queue of start and end events
        PriorityQueue<EventWrapper> orderedEvents = new PriorityQueue<>(
                (e1, e2) -> Math.toIntExact(e1.getTimestamp() - e2.getTimestamp()));
        for (Event<?> event : eventsOfInterest)
            orderedEvents.addAll(event.getWrappers());

        ArrayList<Event<?>> validEvents = new ArrayList<>();
        ArrayList<Frame> framedDataset = new ArrayList<>();
        StateList states = storyboard.getStates();

        while (!orderedEvents.isEmpty()) {
            EventWrapper eventWrapper;
            // Polling all events with equal timestamp
            do {
                eventWrapper = orderedEvents.poll();
                if (eventWrapper.getType() == START_WRAPPER)
                    validEvents.add(eventWrapper.getEvent());
                else
                    validEvents.remove(eventWrapper.getEvent());
            } while (orderedEvents.peek() != null &&
                    orderedEvents.peek().getTimestamp().equals(eventWrapper.getTimestamp()));

            if (validEvents.size() == 0)
                continue;

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

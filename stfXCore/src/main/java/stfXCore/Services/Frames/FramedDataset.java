package stfXCore.Services.Frames;

import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Parsers.ParserFactory;
import stfXCore.Services.Events.EventWrapper;
import stfXCore.Services.StateList;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static stfXCore.Services.Events.EventWrapper.EventType.START_WRAPPER;

public class FramedDataset {

    private Storyboard storyboard;

    private Thresholds thresholds;

    private Long initialTimestamp;

    private Long finalTimestamp;

    public FramedDataset(Storyboard storyboard, Thresholds thresholds) {
        this.storyboard = storyboard;
        this.thresholds = thresholds;

        ArrayList<Long> defaultRange = storyboard.getStates().getTemporalRange();
        this.initialTimestamp = defaultRange.get(0);
        this.finalTimestamp = defaultRange.get(1);
    }

    public FramedDataset restrictInterval(Long initialTimestamp, Long finalTimestamp) {
        if (initialTimestamp != null) this.initialTimestamp = initialTimestamp;
        if (finalTimestamp != null) this.finalTimestamp = finalTimestamp;
        return this;
    }

    private ArrayList<Frame> addUnimportantFrames(ArrayList<Frame> framedDataset) {
        if (framedDataset.size() == 0)
            return framedDataset;

        StateList states = storyboard.getStates();

        // First element case
        if (initialTimestamp < framedDataset.get(0).lowerBound())
            framedDataset.add(0,
                    new UnimportantFrame(states.getStates(initialTimestamp, framedDataset.get(0).lowerBound())));

        // Middle elements
        for (int i = 1; i < framedDataset.size(); ++i) {
            if (framedDataset.get(i - 1).upperBound() < framedDataset.get(i).lowerBound())
                framedDataset.add(i,
                        new UnimportantFrame(states.getStates(framedDataset.get(i - 1).upperBound(), framedDataset.get(i).lowerBound())));
        }

        // Last element case
        if (finalTimestamp > framedDataset.get(framedDataset.size() - 1).upperBound())
            framedDataset.add(framedDataset.size(),
                    new UnimportantFrame(states.getStates(framedDataset.get(framedDataset.size() - 1).upperBound(), finalTimestamp)));

        return framedDataset;
    }

    public ArrayList<Frame> getFrames() {
        ConcurrentLinkedQueue<Event<?>> eventsOfInterest = new ParserFactory(
                storyboard.getRigidTransformations(),
                thresholds.getParameters())
                .restrictInterval(initialTimestamp, finalTimestamp)
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

        return addUnimportantFrames(framedDataset);
    }
}

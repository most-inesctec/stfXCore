package stfXCore.Services.Events;

public class StartEventWrapper extends EventWrapper {

    private Event event;

    public StartEventWrapper(Event event) {
        super(EventType.START_WRAPPER, event);
    }
}

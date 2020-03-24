package stfXCore.Services.Events;

public class EndEventWrapper extends EventWrapper {

    private Event event;

    public EndEventWrapper(Event event) {
        super(EventType.END_WRAPPER, event);
    }
}
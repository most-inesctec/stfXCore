package stfXCore.Services.Events;

public class EndEventWrapper extends EventWrapper {

    public EndEventWrapper(Event event) {
        super(EventType.END_WRAPPER, event);
    }
}
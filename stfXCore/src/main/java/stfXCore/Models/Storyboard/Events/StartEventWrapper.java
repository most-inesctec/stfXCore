package stfXCore.Models.Storyboard.Events;

public class StartEventWrapper extends EventWrapper {

    public StartEventWrapper(Event event) {
        super(EventType.START_WRAPPER, event);
    }
}

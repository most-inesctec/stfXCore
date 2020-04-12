package stfXCore.Services.Events;

import lombok.Data;

@Data
public abstract class EventWrapper {

    public enum EventType {
        START_WRAPPER,
        END_WRAPPER
    }

    private EventType type;

    private Event event;

    private Float timestamp;

    public EventWrapper(EventType type, Event event) {
        this.type = type;
        this.event = event;
    }

    public EventWrapper setTimestamp(Float timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}

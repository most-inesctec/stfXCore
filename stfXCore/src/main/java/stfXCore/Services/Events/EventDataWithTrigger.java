package stfXCore.Services.Events;

import lombok.Data;

@Data
public class EventDataWithTrigger<T> extends EventData {

    private T trigger;

    public EventDataWithTrigger(EventData data, T trigger) {
        super(data.getThreshold(), data.getType());
        this.trigger = trigger;
    }

}

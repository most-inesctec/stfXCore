package stfXCore.Services.Events;

import lombok.Data;

@Data
public class EventDataWithTrigger extends EventData {

    private Float triggerValue;

    public EventDataWithTrigger(EventData data, Float triggerValue) {
        super(data.getTrigger(), data.getType());
        this.triggerValue = triggerValue;
    }

}

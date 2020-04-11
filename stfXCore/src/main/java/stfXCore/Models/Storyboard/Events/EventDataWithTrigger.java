package stfXCore.Models.Storyboard.Events;

import lombok.Data;

@Data
public class EventDataWithTrigger<T> extends EventData {

    private T triggerValue;

    public EventDataWithTrigger(EventData data, T triggerValue) {
        super(data.getTrigger(), data.getType());
        this.triggerValue = triggerValue;
    }

}

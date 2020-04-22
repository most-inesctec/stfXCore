package stfXCore.Services.Events;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EventDataWithTrigger<T> extends EventData {

    private T trigger;

    public EventDataWithTrigger(EventData data, T trigger) {
        super(data.getThreshold(), data.getType());
        this.trigger = trigger;
    }

}

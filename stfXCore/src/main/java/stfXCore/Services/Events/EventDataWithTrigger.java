package stfXCore.Services.Events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EventDataWithTrigger<T> extends EventData {

    @JsonIgnore
    private EventData originalData;

    private T trigger;

    public EventDataWithTrigger(EventData data, T trigger) {
        super(data.getThreshold(), data.getType());
        this.originalData = data;
        this.trigger = trigger;
    }

    public EventDataWithTrigger joinEvents(EventDataWithTrigger event) {
        return new EventDataWithTrigger<T>(originalData, trigger + trigger);
    }
}

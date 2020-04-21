package stfXCore.Services.Events;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class EventData {

    private Event.ThresholdTrigger threshold;

    private Event.Transformation type;

    EventData(Event.ThresholdTrigger threshold, Event.Transformation type) {
        this.threshold = threshold;
        this.type = type;
    }
}

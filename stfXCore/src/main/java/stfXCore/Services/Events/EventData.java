package stfXCore.Services.Events;

import lombok.Data;

@Data
public class EventData {

    private Event.ThresholdTrigger threshold;

    private Event.Transformation type;

    EventData(Event.ThresholdTrigger threshold, Event.Transformation type) {
        this.threshold = threshold;
        this.type = type;
    }
}

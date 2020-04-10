package stfXCore.Models.Storyboard.Events;

import lombok.Data;

@Data
public class EventData {

    private Event.ThresholdTrigger trigger;

    private Event.Transformation type;

    EventData(Event.ThresholdTrigger trigger, Event.Transformation type) {
        this.trigger = trigger;
        this.type = type;
    }
}

package stfXCore.Services.Events;

import lombok.Data;
import stfXCore.Utils.Pair;

import java.util.ArrayList;

@Data
public class EventData {

    private Event.ThresholdTrigger trigger;

    private Event.Transformation type;

    /**
     * First Element are timestamps
     * Second Element are triggerValues
     */
    private ArrayList<Pair<Float, Float>> values;

    EventData(Event.ThresholdTrigger trigger, Event.Transformation type) {
        this.trigger = trigger;
        this.type = type;
    }
}

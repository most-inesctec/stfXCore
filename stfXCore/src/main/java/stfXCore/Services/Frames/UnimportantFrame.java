package stfXCore.Services.Frames;

import stfXCore.Models.State;
import stfXCore.Services.Events.EventDataWithTrigger;
import stfXCore.Services.Events.UnimportantEvent;

import java.util.List;

public class UnimportantFrame extends Frame {

    public UnimportantFrame(List<State> phenomena) {
        super(phenomena);
        this.addEvent(new EventDataWithTrigger(new UnimportantEvent().getData(), null));
    }
}

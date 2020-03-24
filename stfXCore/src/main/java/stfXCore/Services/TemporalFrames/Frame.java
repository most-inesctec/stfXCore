package stfXCore.Services.TemporalFrames;

import lombok.Data;
import stfXCore.Models.Storyboard.State;
import stfXCore.Utils.Pair;

import java.util.ArrayList;

@Data
public class Frame {

    private ArrayList<State> phenomena;

    private ArrayList<EventData> events;

    public Pair<Float, Float> getTemporalRange() {
        return new Pair<Float, Float>(
                this.phenomena.get(0).getTimestamp(),
                this.phenomena.get(phenomena.size() - 1).getTimestamp()
        );
    }
}

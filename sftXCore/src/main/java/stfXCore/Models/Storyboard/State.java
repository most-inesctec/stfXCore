package stfXCore.Models.Storyboard;

import lombok.Data;

import java.util.ArrayList;

@Data
public class State {

    private ArrayList<ArrayList<Float>> representation;

    private float timestamp;

    State() {}

    State(ArrayList<ArrayList<Float>> representation, float timestamp) {
        this.representation = representation;
        this.timestamp = timestamp;
    }
}

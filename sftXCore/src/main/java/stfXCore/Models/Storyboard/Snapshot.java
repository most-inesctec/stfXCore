package stfXCore.Models.Storyboard;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Snapshot {

    private State X;
    private State Y;

    public Snapshot() {}

    public Snapshot setX(ArrayList<ArrayList<Float>> X, float timestamp) {
        this.X = new State(X, timestamp);
        return this;
    }

    public Snapshot setY(ArrayList<ArrayList<Float>> Y, float timestamp) {
        this.Y = new State(Y, timestamp);
        return this;
    }

}

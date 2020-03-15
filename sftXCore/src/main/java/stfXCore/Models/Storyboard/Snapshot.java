package stfXCore.Models.Storyboard;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class Snapshot implements Serializable {

    private State X;
    private State Y;

    public Snapshot() {}

    public Snapshot(State X, State Y) {
        this.X = X;
        this.Y = Y;
    }

    public Snapshot setX(ArrayList<ArrayList<Float>> X, float timestamp) {
        this.X = new State(X, timestamp);
        return this;
    }

    public Snapshot setY(ArrayList<ArrayList<Float>> Y, float timestamp) {
        this.Y = new State(Y, timestamp);
        return this;
    }
}

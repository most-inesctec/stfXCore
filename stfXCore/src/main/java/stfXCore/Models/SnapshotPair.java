package stfXCore.Models;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class SnapshotPair implements Serializable {

    private Snapshot X;
    private Snapshot Y;

    public SnapshotPair() {
    }

    public SnapshotPair(Snapshot X, Snapshot Y) {
        this.X = X;
        this.Y = Y;
    }

    public SnapshotPair setX(ArrayList<ArrayList<Float>> X, Long timestamp) {
        this.X = new Snapshot(X, timestamp);
        return this;
    }

    public SnapshotPair setY(ArrayList<ArrayList<Float>> Y, Long timestamp) {
        this.Y = new Snapshot(Y, timestamp);
        return this;
    }
}

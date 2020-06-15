package stfXCore.Models;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TimelessSnapshotPair {

    private ArrayList<ArrayList<Float>> X;
    private ArrayList<ArrayList<Float>> Y;

    public TimelessSnapshotPair() {
    }

    public TimelessSnapshotPair(SnapshotPair s) {
        this.X = s.getX().getRepresentation();
        this.Y = s.getY().getRepresentation();
    }
}

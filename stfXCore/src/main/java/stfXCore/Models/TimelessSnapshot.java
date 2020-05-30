package stfXCore.Models;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TimelessSnapshot {

    private ArrayList<ArrayList<Float>> X;
    private ArrayList<ArrayList<Float>> Y;

    public TimelessSnapshot() {
    }

    public TimelessSnapshot(Snapshot s) {
        this.X = s.getX().getRepresentation();
        this.Y = s.getY().getRepresentation();
    }
}

package stfXCore.Models.Storyboard;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Snapshot {

    private ArrayList<ArrayList<Float>> X;
    private ArrayList<ArrayList<Float>> Y;
    private float[] validTime;

    Snapshot() {}

    public Snapshot(ArrayList<ArrayList<Float>> X, ArrayList<ArrayList<Float>> Y, float[] validTime) {
        this.X = X;
        this.Y = Y;
        this.validTime = validTime;
    }

}

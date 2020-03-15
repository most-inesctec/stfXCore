package stfXCore.Models.Storyboard;

import lombok.Data;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.ArrayList;

@Data
public class State implements Serializable {

    @Lob
    private ArrayList<ArrayList<Float>> representation;

    private float timestamp;

    State() {}

    State(ArrayList<ArrayList<Float>> representation, float timestamp) {
        this.representation = representation;
        this.timestamp = timestamp;
    }
}

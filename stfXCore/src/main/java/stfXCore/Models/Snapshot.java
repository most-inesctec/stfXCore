package stfXCore.Models;

import lombok.Data;

import javax.persistence.Lob;
import java.io.Serializable;
import java.util.ArrayList;

@Data
public class Snapshot implements Serializable {

    @Lob
    private ArrayList<ArrayList<Float>> representation;

    private Long timestamp;

    Snapshot() {}

    Snapshot(ArrayList<ArrayList<Float>> representation, Long timestamp) {
        this.representation = representation;
        this.timestamp = timestamp;
    }
}

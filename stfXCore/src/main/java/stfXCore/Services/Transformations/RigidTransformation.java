package stfXCore.Services.Transformations;

import lombok.Builder;
import lombok.Data;
import stfXCore.Models.Storyboard.Snapshot;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.io.Serializable;
import java.util.ArrayList;

@Data
public class RigidTransformation implements Serializable {

    // TODO: This should not be here. The Storyboard should rather store a ArrayList<Pair<Snapshot, Transformation>>
    protected Snapshot snapshot;

    private float rotation;

    private ArrayList<Float> translation;

    /**
     * Rigid registration only considers uniform scaling
     */
    private float scale;

    @Builder
    public RigidTransformation() {}
}

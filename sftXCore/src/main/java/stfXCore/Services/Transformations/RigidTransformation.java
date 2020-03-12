package stfXCore.Services.Transformations;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class RigidTransformation extends Transformation implements Serializable {

    private float rotation;

    private ArrayList<Float> translation;

    /**
     * Rigid registration only considers uniform scaling
     */
    private float scale;

    public RigidTransformation() {}
}

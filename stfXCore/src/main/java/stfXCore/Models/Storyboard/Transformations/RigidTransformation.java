package stfXCore.Models.Storyboard.Transformations;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class RigidTransformation implements Serializable {

    private float rotation;

    private ArrayList<Float> translation;

    /**
     * Rigid registration only considers uniform scaling
     */
    private float scale;

    public RigidTransformation() {
    }

    public boolean isNull() {
        return rotation == 0 &&
                translation.get(0) == 0 && translation.get(1) == 0 &&
                scale == 1;
    }
}

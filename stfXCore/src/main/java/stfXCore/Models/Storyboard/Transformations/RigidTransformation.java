package stfXCore.Models.Storyboard.Transformations;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class RigidTransformation implements Serializable {

    private ArrayList<Float> translation;

    /**
     * Rigid registration only considers uniform scaling
     */
    private float scale;

    private float rotation;

    public RigidTransformation() {
    }

    public RigidTransformation(ArrayList<Float> translation, float scale, float rotation) {
        this.translation = translation;
        this.scale = scale;
        this.rotation = rotation;
    }

    public boolean isNull() {
        return rotation == 0 &&
                translation.get(0) == 0 && translation.get(1) == 0 &&
                scale == 1;
    }
}

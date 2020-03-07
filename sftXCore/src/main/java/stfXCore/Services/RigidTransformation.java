package stfXCore.Services;

import lombok.Data;

@Data
public class RigidTransformation {

    private float rotation;

    private float[] translation;

    /**
     * Rigid registration only considers uniform scaling
     */
    private float scale;

    RigidTransformation() {}
}

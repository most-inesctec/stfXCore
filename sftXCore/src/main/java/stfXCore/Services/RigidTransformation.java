package stfXCore.Services;

import lombok.Data;

import java.io.Serializable;

@Data
public class RigidTransformation implements Serializable {

    private float rotation;

    private float[] translation;

    /**
     * Rigid registration only considers uniform scaling
     */
    private float scale;

    public RigidTransformation() {}
}

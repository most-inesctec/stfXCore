package stfXCore.Services.Transformations;

import lombok.Data;
import stfXCore.Models.Storyboard.Snapshot;

@Data
public abstract class Transformation {

    private Snapshot snapshot;

    Transformation() {};
}

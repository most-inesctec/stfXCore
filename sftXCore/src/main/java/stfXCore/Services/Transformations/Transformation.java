package stfXCore.Services.Transformations;

import lombok.Data;
import stfXCore.Models.Storyboard.Snapshot;

import javax.persistence.MappedSuperclass;


@Data
@MappedSuperclass
public abstract class Transformation {

    protected Snapshot snapshot;

    Transformation() {};
}

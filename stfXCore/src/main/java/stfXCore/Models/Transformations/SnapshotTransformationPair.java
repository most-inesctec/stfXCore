package stfXCore.Models.Transformations;

import lombok.Data;
import stfXCore.Models.Snapshot;

import javax.persistence.*;

@Entity
@Data
public class SnapshotTransformationPair {

    @Lob
    Snapshot first;

    @Column(length = 511)
    RigidTransformation second;

    /**
     * Primary key automatically populated by the JPA provider.
     */
    @Id
    @GeneratedValue
    private Long id;

    public SnapshotTransformationPair() {
    }

    public SnapshotTransformationPair(Snapshot snapshot, RigidTransformation transformation) {
        this.first = snapshot;
        this.second = transformation;
    }
}

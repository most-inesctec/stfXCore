package stfXCore.Models.Transformations;

import lombok.Data;
import stfXCore.Models.SnapshotPair;

import javax.persistence.*;

@Entity
@Data
public class SnapshotTransformationPair {

    @Lob
    SnapshotPair first;

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

    public SnapshotTransformationPair(SnapshotPair snapshotPair, RigidTransformation transformation) {
        this.first = snapshotPair;
        this.second = transformation;
    }
}

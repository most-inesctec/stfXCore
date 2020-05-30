package stfXCore.Models;

import lombok.Data;
import stfXCore.Models.Transformations.RigidTransformation;
import stfXCore.Models.Transformations.SnapshotTransformationPair;
import stfXCore.Services.StateList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lombok annotation to create all the getters, setters, equals, hash, and toString methods, based on the fields
 */
@Data
/**
 * JPA annotation to make this object ready for storage in a JPA-based data store
 */
@Entity
public class Storyboard {

    /**
     * Primary key automatically populated by the JPA provider.
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * Attribute stored as BLOB
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<SnapshotTransformationPair> rigidTransformations;

    @Column(length = 511)
    private DatasetMetadata metadata;

    public Storyboard() {
    }

    public Storyboard(DatasetMetadata metadata) {
        this.metadata = metadata;
        rigidTransformations = new ArrayList<>();
    }

    public void addRigidTransformation(Snapshot snapshot, RigidTransformation rt) {
        rigidTransformations.add(new SnapshotTransformationPair(snapshot, rt));
    }

    public StateList getStates() {
        return new StateList(rigidTransformations);
    }
}

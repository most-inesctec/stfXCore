package stfXCore.Models.Storyboard;

import lombok.Data;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Services.StateList;
import stfXCore.Utils.Pair;

import javax.persistence.*;
import java.util.ArrayList;

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
    @Lob
    private ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations;

    private DatasetMetadata metadata;

    public Storyboard() {}

    public Storyboard(DatasetMetadata metadata) {
        this.metadata = metadata;
        rigidTransformations = new ArrayList<>();
    }

    public void addRigidTransformation(Snapshot snapshot, RigidTransformation rt) {
        rigidTransformations.add(new Pair<>(snapshot, rt));
    }

    public StateList getStates() {
        return new StateList(rigidTransformations);
    }
}

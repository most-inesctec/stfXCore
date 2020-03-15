package stfXCore.Models.Storyboard;

import lombok.Data;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
    private ArrayList<RigidTransformation> rigidTransformations;

    public Storyboard() { }

    public Storyboard(Dataset dataset) {
        rigidTransformations = new ArrayList<>();
    }

    public void addRigidTransformation(RigidTransformation rt) {
        rigidTransformations.add(rt);
    }

}

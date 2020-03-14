package stfXCore.Models.Storyboard;

import lombok.Data;
import stfXCore.Services.Transformations.RigidTransformation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
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
    private ArrayList<RigidTransformation> rigidTransformations;

    public Storyboard() { }

    public Storyboard(Dataset dataset) {
        rigidTransformations = new ArrayList<>();
    }

    public void addRigidTransformation(RigidTransformation rt) {
        rigidTransformations.add(rt);
    }
}

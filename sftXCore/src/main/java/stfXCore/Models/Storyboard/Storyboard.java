package stfXCore.Models.Storyboard;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import stfXCore.Services.RigidTransformation;
import org.springframework.core.env.Environment;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    private ArrayList<RigidTransformation> rigidTransformations;

//    @Autowired
//    private Environment env;


    public Storyboard() { }

    public Storyboard(Dataset dataset) {
        rigidTransformations = new ArrayList<>();
    }

    public void addRigidTransformation(RigidTransformation rt) {
        rigidTransformations.add(rt);
    }
}

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
        computeTransformations(dataset);
    }

    /**
     * Compute the transformations by calling the algorithm enpoint
     */
    // TODO: A model calling an endpoint? Hmmmm
    private void computeTransformations(Dataset dataset) {
        //String uri = env.getProperty("PSR_endpoint") + "/cpd";
        String uri = "http://localhost:5000/cpd";
        ArrayList<ArrayList<ArrayList<Float>>> snapshots = dataset.getDataset();

        // Making it synchronous for now
        for (int i = 0; i < snapshots.size() - 1; ++i) {
            RestTemplate restTemplate = new RestTemplate();
            rigidTransformations.add(
                restTemplate.postForObject(
                        uri,
                        new Snapshot(snapshots.get(i), snapshots.get(i + 1)),
                        RigidTransformation.class));
        }
    }
}

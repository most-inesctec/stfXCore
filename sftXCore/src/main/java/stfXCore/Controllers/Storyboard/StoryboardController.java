package stfXCore.Controllers.Storyboard;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import stfXCore.Models.Storyboard.Dataset;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Repositories.StoryboardRepository;
import stfXCore.Services.RigidTransformation;

import java.util.ArrayList;

@RestController
public class StoryboardController {

    private final StoryboardRepository repository;

    // Not adding Assembler for now, IMO it might not make sense
    StoryboardController(StoryboardRepository repository) {
        this.repository = repository;
    }

    /**
     * Compute the transformations by calling the algorithm enpoint
     */
    private void computeTransformations(Dataset dataset, Storyboard storyboard) {
        //String uri = env.getProperty("PSR_endpoint") + "/cpd";
        String uri = "http://localhost:5000/cpd";
        ArrayList<ArrayList<ArrayList<Float>>> snapshots = dataset.getDataset();

        // Making it synchronous for now
        for (int i = 0; i < snapshots.size() - 1; ++i) {
            RestTemplate restTemplate = new RestTemplate();
            storyboard.addRigidTransformation(
                    restTemplate.postForObject(
                            uri,
                            new Snapshot(snapshots.get(i), snapshots.get(i + 1)),
                            RigidTransformation.class));
        }
    }

    @PostMapping("/storyboard")
    public Long newStoryboard(@RequestBody Dataset dataset) {
        System.out.print(dataset);
        Storyboard storyboard = new Storyboard(dataset);
        computeTransformations(dataset, storyboard);
        return repository.save(new Storyboard(dataset)).getId();
    }

//    @PostMapping("/storyboard/{id}")
//    public
//
//
//

    @DeleteMapping("/storyboard/{id}")
    void deleteStoryboard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}

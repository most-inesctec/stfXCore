package stfXCore.Controllers.Storyboard;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import stfXCore.Models.Storyboard.Dataset;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.StoryboardNotFoundException;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Models.Storyboard.Thresholds.ThresholdsWrapper;
import stfXCore.Repositories.StoryboardRepository;
import stfXCore.Services.RigidTransformation;

import java.util.ArrayList;

@RestController
public class StoryboardController {

//    @Autowired
//    private Environment env;

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
        String methodUri = "http://localhost:5000/cpd";
        ArrayList<ArrayList<ArrayList<Float>>> snapshots = dataset.getDataset();

        // Making it synchronous for now
        for (int i = 0; i < snapshots.size() - 1; ++i) {
            RestTemplate restTemplate = new RestTemplate();
            storyboard.addRigidTransformation(
                    restTemplate.postForObject(
                            methodUri,
                            new Snapshot(snapshots.get(i), snapshots.get(i + 1)),
                            RigidTransformation.class));
        }
    }

    @PostMapping("/storyboard")
    public Long newStoryboard(@RequestBody Dataset dataset) {
        Storyboard storyboard = new Storyboard(dataset);
        computeTransformations(dataset, storyboard);
        return repository.save(storyboard).getId();
    }

    @PostMapping("/storyboard/{id}")
    public void getEventsOfInterest(@PathVariable Long id, @RequestBody ThresholdsWrapper thresholds) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        // TODO
        System.out.print(thresholds);
    }

    @DeleteMapping("/storyboard/{id}")
    void deleteStoryboard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}

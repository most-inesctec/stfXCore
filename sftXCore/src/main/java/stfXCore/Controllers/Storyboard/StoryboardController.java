package stfXCore.Controllers.Storyboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import stfXCore.Models.Storyboard.Dataset;
import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Models.Storyboard.StoryboardNotFoundException;
import stfXCore.Models.Storyboard.Thresholds.ThresholdsWrapper;
import stfXCore.Repositories.StoryboardRepository;
import stfXCore.Services.EventsFilter;
import stfXCore.Services.RigidTransformation;

import java.util.ArrayList;

@RestController
public class StoryboardController {

    @Autowired
    private Environment env;

    private final StoryboardRepository repository;

    // Not adding Assembler for now, IMO it might not make sense
    StoryboardController(StoryboardRepository repository) {
        this.repository = repository;
    }

    /**
     * Compute the transformations by calling the algorithm enpoint
     */
    private void computeTransformations(Dataset dataset, Storyboard storyboard) {
        String methodUri = env.getProperty("PSR_endpoint");

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
    public void getEventsOfInterest(@PathVariable Long id, @RequestBody ThresholdsWrapper thresholdsWrapper) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        EventsFilter.filter(storyboard.getRigidTransformations(), thresholdsWrapper.getThresholds());
    }

    @DeleteMapping("/storyboard/{id}")
    void deleteStoryboard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}

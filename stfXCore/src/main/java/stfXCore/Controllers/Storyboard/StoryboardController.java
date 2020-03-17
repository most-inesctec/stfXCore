package stfXCore.Controllers.Storyboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import stfXCore.Models.Storyboard.*;
import stfXCore.Models.Storyboard.ErrorHandlers.StoryboardMissingInformationException;
import stfXCore.Models.Storyboard.ErrorHandlers.StoryboardNotFoundException;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Repositories.StoryboardRepository;
import stfXCore.Services.Events.Event;
import stfXCore.Services.Events.EventParser;
import stfXCore.Services.Transformations.RigidTransformation;

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
     *
     * @param dataset    The input dataset
     * @param storyboard The storyboard that stores the computed transformations
     */
    private void computeTransformations(Dataset dataset, Storyboard storyboard) {
        ArrayList<ArrayList<ArrayList<Float>>> snapshots = dataset.getDataset();
        String methodUri = env.getProperty("PSR_endpoint");
        float timePeriod = dataset.getMetadata().getTimePeriod();

        // Making it synchronous for now
        for (int i = 0; i < snapshots.size() - 1; ++i) {
            RestTemplate restTemplate = new RestTemplate();

            Snapshot snapshot = new Snapshot()
                    .setX(snapshots.get(i), timePeriod * i)
                    .setY(snapshots.get(i + 1), timePeriod * (i+1));
            RigidTransformation rt = restTemplate.postForObject(
                    methodUri, new TimelessSnapshot(snapshot), RigidTransformation.class);
            rt.setSnapshot(snapshot);

            storyboard.addRigidTransformation(rt);
        }
    }

    @PostMapping("/storyboard")
    public Long newStoryboard(@RequestBody Dataset dataset) {
        Storyboard storyboard = new Storyboard(dataset);
        if (dataset.getDataset() == null || dataset.getMetadata() == null)
            throw new StoryboardMissingInformationException();

        computeTransformations(dataset, storyboard);
        return repository.save(storyboard).getId();
    }

    @PostMapping("/storyboard/{id}")
    public ArrayList<Event> getEventsOfInterest(@PathVariable Long id, @RequestBody Thresholds thresholds) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        return EventParser.parseTransformations(storyboard.getRigidTransformations(), thresholds.getParameters());
    }

    @DeleteMapping("/storyboard/{id}")
    void deleteStoryboard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}

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
import stfXCore.Services.SnapshotsBuilder;
import stfXCore.Services.Frames.Frame;
import stfXCore.Services.Frames.FramedDataset;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Models.Storyboard.Transformations.TransformationList;

import java.util.ArrayList;
import java.util.stream.Collectors;

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
        String methodUri = env.getProperty("PSR_endpoint");
        ArrayList<Snapshot> snapshots = SnapshotsBuilder.createSnapshots(dataset);
        ArrayList<TimelessSnapshot> representations = snapshots.stream()
                .map(TimelessSnapshot::new)
                .collect(Collectors.toCollection(ArrayList::new));

        // For each snapshot exists a transformation
        ArrayList<RigidTransformation> transformations = new RestTemplate().postForObject(
                methodUri, representations, TransformationList.class).getTransformations();

        // Rather have two n loops than performing n request to the API
        for (int i = 0; i < snapshots.size(); ++i)
            storyboard.addRigidTransformation(snapshots.get(i), transformations.get(i));
    }

    @PostMapping("/storyboard")
    public Long newStoryboard(@RequestBody Dataset dataset) {
        if (dataset.getDataset() == null ||
                dataset.getMetadata() == null ||
                dataset.getMetadata().getTimePeriod() == null)
            throw new StoryboardMissingInformationException();

        Storyboard storyboard = new Storyboard(dataset.getMetadata());
        computeTransformations(dataset, storyboard);
        return repository.save(storyboard).getId();
    }

    @PostMapping("/storyboard/{id}")
    public ArrayList<Frame> getEventsOfInterest(@PathVariable Long id, @RequestBody Thresholds thresholds) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        if (thresholds.getParameters() == null)
            throw new StoryboardMissingInformationException();

        return FramedDataset.getFrames(storyboard, thresholds);
    }

    @GetMapping("/storyboard/metadata/{id}")
    public DatasetMetadata getMetadata(@PathVariable Long id) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        return storyboard.getMetadata();
    }

    @DeleteMapping("/storyboard/{id}")
    void deleteStoryboard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}

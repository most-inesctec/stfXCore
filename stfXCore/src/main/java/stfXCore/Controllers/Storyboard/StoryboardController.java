package stfXCore.Controllers.Storyboard;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import stfXCore.Models.Storyboard.*;
import stfXCore.Models.Storyboard.ErrorHandlers.StoryboardBadFileException;
import stfXCore.Models.Storyboard.ErrorHandlers.StoryboardMissingInformationException;
import stfXCore.Models.Storyboard.ErrorHandlers.StoryboardNotFoundException;
import stfXCore.Models.Storyboard.Thresholds.Thresholds;
import stfXCore.Repositories.StoryboardRepository;
import stfXCore.Services.SnapshotsBuilder;
import stfXCore.Services.Frames.Frame;
import stfXCore.Services.Frames.FramedDataset;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Models.Storyboard.Transformations.TransformationList;

import java.io.IOException;
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

        // Rather have two O(n) loops than performing n request to the API
        for (int i = 0; i < snapshots.size(); ++i)
            storyboard.addRigidTransformation(snapshots.get(i), transformations.get(i));
    }

    private Long createStoryboard(Dataset dataset) {
        if (dataset.getDataset() == null ||
                dataset.getMetadata() == null ||
                dataset.getMetadata().getTimePeriod() == null ||
                dataset.getMetadata().getStartTime() == null)
            throw new StoryboardMissingInformationException();

        Storyboard storyboard = new Storyboard(dataset.getMetadata());
        computeTransformations(dataset, storyboard);
        return repository.save(storyboard).getId();
    }

    @PostMapping("/storyboard")
    public Long newStoryboard(@RequestBody Dataset dataset) {
        return createStoryboard(dataset);
    }

    @PostMapping("/storyboard/file")
    public Long newStoryboardFromFile(@RequestParam("dataset") MultipartFile datasetFile) {
        if (datasetFile.isEmpty())
            throw new StoryboardMissingInformationException();
        try {
            return createStoryboard(
                    new Gson().fromJson(
                            new String(datasetFile.getBytes()), Dataset.class));
        } catch (IOException e) {
            throw new StoryboardBadFileException();
        }
    }

    @GetMapping("/storyboard/metadata/{id}")
    public DatasetMetadata getMetadata(@PathVariable Long id) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        return storyboard.getMetadata();
    }

    @PostMapping("/storyboard/{id}")
    public ArrayList<Frame> getEventsOfInterest(@PathVariable Long id,
                                                @RequestBody Thresholds thresholds,
                                                @RequestParam(required = false) Long initalTimestamp,
                                                @RequestParam(required = false) Long finalTimestamp) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        if (thresholds.getParameters() == null)
            throw new StoryboardMissingInformationException();

        return new FramedDataset(storyboard, thresholds).getFrames(initalTimestamp, finalTimestamp);
    }

    @DeleteMapping("/storyboard/{id}")
    void deleteStoryboard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}

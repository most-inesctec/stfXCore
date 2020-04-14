package stfXCore.Controllers.Storyboard;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.json.GsonJsonParser;
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

import javax.validation.Valid;
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

    private Dataset loadAndVerifyDataset(Dataset dataset, MultipartFile datasetFile) {
        Dataset loadedDataset;

        if (datasetFile == null || datasetFile.isEmpty())
            loadedDataset = dataset;
        else try {
            loadedDataset = new Gson().fromJson(
                    new String(datasetFile.getBytes()), Dataset.class);
        } catch (IOException e) {
            throw new StoryboardBadFileException();
        }

        if (loadedDataset.getDataset() == null ||
                loadedDataset.getMetadata() == null ||
                loadedDataset.getMetadata().getTimePeriod() == null)
            throw new StoryboardMissingInformationException();

        return loadedDataset;
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
    public Long newStoryboard(@Valid Dataset dataset,
                              @RequestParam(name = "file", required = false) MultipartFile datasetFile) {
        Dataset verifiedDataset = loadAndVerifyDataset(dataset, datasetFile);
        Storyboard storyboard = new Storyboard(verifiedDataset.getMetadata());
        computeTransformations(verifiedDataset, storyboard);
        return repository.save(storyboard).getId();
    }

    @GetMapping("/storyboard/metadata/{id}")
    public DatasetMetadata getMetadata(@PathVariable Long id) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        return storyboard.getMetadata();
    }

    @PostMapping("/storyboard/{id}")
    public ArrayList<Frame> getEventsOfInterest(@PathVariable Long id, @RequestBody Thresholds thresholds) {
        Storyboard storyboard = repository.findById(id)
                .orElseThrow(() -> new StoryboardNotFoundException(id));

        if (thresholds.getParameters() == null)
            throw new StoryboardMissingInformationException();

        return FramedDataset.getFrames(storyboard, thresholds);
    }

    @DeleteMapping("/storyboard/{id}")
    void deleteStoryboard(@PathVariable Long id) {
        repository.deleteById(id);
    }

}

package stfXCore.Controllers.Storyboard;

import org.springframework.web.bind.annotation.*;
import stfXCore.Models.Storyboard.Dataset;
import stfXCore.Models.Storyboard.Storyboard;
import stfXCore.Repositories.StoryboardRepository;

@RestController
public class StoryboardController {

    private final StoryboardRepository repository;

    // Not adding Assembler for now, IMO it might not make sense
    StoryboardController(StoryboardRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/storyboard")
    public Long newStoryboard(@RequestBody Dataset dataset) {
        System.out.print(dataset);
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

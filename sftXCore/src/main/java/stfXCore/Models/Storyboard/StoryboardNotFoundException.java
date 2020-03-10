package stfXCore.Models.Storyboard;

public class StoryboardNotFoundException extends RuntimeException {

    public StoryboardNotFoundException(Long id) {
        super("Could not find storyboard " + id);
    }
}

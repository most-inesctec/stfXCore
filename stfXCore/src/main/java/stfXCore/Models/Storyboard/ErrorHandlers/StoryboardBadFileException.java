package stfXCore.Models.Storyboard.ErrorHandlers;

public class StoryboardBadFileException extends RuntimeException {

    public StoryboardBadFileException() {
        super("Unable to load dataset from file.");
    }
}
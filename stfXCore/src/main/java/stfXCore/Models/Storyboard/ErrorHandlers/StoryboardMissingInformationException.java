package stfXCore.Models.Storyboard.ErrorHandlers;

public class StoryboardMissingInformationException extends RuntimeException {

    public StoryboardMissingInformationException() {
        super("Missing information in the performed request.");
    }
}
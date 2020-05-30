package stfXCore.Models.ErrorHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StoryboardMissingInformationAdvice {

    @ResponseBody
    @ExceptionHandler(StoryboardMissingInformationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String storyboardMissingInformationHandler(StoryboardMissingInformationException ex) {
        return ex.getMessage();
    }
}

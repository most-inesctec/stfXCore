package stfXCore.Models.ErrorHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StoryboardBadFileAdvice {

    @ResponseBody
    @ExceptionHandler(StoryboardBadFileException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String storyboardMissingInformationHandler(StoryboardBadFileException ex) {
        return ex.getMessage();
    }
}

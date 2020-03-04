package stfXCore.Controllers.MatLabClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class MatLabController {

    @GetMapping("/matlab-test")
    public void matlabTest() {
        // Call microservice
    }
}

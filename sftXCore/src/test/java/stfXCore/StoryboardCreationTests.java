package stfXCore;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoryboardCreationTests {

    private ClientAndServer mockServer;

    @BeforeEach
    public void startServer() {
        mockServer = startClientAndServer(5000);
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    @Test
    public void testStoryboardCreation(@Autowired MockMvc mvc) throws Exception {
        new MockServerClient("127.0.0.1", 5000)
                .when(request()
                                .withMethod("POST")
                                .withPath("/cpd")
                                .withHeader("\"Content-type\", \"application/json\"")
                                .withBody(exact("{\"x\":[[49.448746,49.44956],[53.430676,49.449554],[53.434605,53.427555],[51.43714,55.40298],[49.443207,53.429226],[49.448746,49.44956]],\"y\":[[50.0,50.0],[54.0,50.0],[54.0,54.0],[52.0,56.0],[50.0,54.0],[50.0,50.0]]}")))
                .respond(response()
                        .withStatusCode(200)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(exact("{\"rotation\": 0.00020695867664263134, \"scale\": 0.9943084085430711, \"translation\": [-0.26522648780481717, -0.2675864390243703]}"))
                        .withDelay(TimeUnit.MILLISECONDS, 500));

        mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }
}

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.concurrent.TimeUnit;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoryboardEndpointTests {

    private ClientAndServer mockServer;

    @BeforeEach
    public void startServer() {
        mockServer = startClientAndServer(5000);
    }

    @AfterEach
    public void stopServer() {
        mockServer.stop();
    }

    private void mockCPD() {
        new MockServerClient("127.0.0.1", 5000)
                .when(request()
                        .withMethod("POST")
                        .withPath("/cpd-all")
                        .withHeader("\"Content-type\", \"application/json\"")
                        .withBody(exact("[{\"x\":[[49.448746,49.44956],[53.430676,49.449554],[53.434605,53.427555],[51.43714,55.40298],[49.443207,53.429226],[49.448746,49.44956]],\"y\":[[50.0,50.0],[54.0,50.0],[54.0,54.0],[52.0,56.0],[50.0,54.0],[50.0,50.0]]}]")))
                .respond(response()
                        .withStatusCode(200)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(exact("{\"transformations\": [{\"rotation\": 0.00020695867664263134, \"scale\": 0.9943084085430711, \"translation\": [-0.26522648780481717, -0.2675864390243703]}]}"))
                        .withDelay(TimeUnit.MILLISECONDS, 500));
    }

    @Test
    public void testStoryboardCreation(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3}}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testStoryboardBadInput(@Autowired MockMvc mvc) throws Exception {
        // Missing dataset
        mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"metadata\": {\"timePeriod\": 3}}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));

        // Missing metadata
        mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]]}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));
    }

    @Test
    public void testStoryboardNotFound(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(post("/storyboard/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find storyboard 3"));
    }

    @Test
    public void testThresholdsMissingInput(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3}}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // No content
        mvc.perform(post("/storyboard/" + id))
                .andExpect(status().isBadRequest());

        // No thresholds
        mvc.perform(post("/storyboard/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));
    }

    @Test
    public void testDeleteStoryboard(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3}}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(delete("/storyboard/" + id))
                .andExpect(status().isOk());
    }
}

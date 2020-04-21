package stfXCore;

import org.junit.jupiter.api.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.TimeUnit;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ImmutabilityTest extends MockTemplate {

    protected void mockCPDIdentification() {
        new MockServerClient("127.0.0.1", 5000)
                .when(request()
                        .withMethod("POST")
                        .withPath("/cpd-all")
                        .withHeader("\"Content-type\", \"application/json\"")
                        .withBody(exact("[{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]}]")))
                .respond(response()
                        .withStatusCode(200)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(exact("{\"transformations\": [{\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}, {\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}, {\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}, {\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}]}"))
                        .withDelay(TimeUnit.MILLISECONDS, 500));
    }

    protected void mockCPDComplexIdentification() {
        new MockServerClient("127.0.0.1", 5000)
                .when(request()
                        .withMethod("POST")
                        .withPath("/cpd-all")
                        .withHeader("\"Content-type\", \"application/json\"")
                        .withBody(exact("[{\"x\":[[-2.0,-2.0],[-2.0,-1.0],[-1.0,-2.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]]},{\"x\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"y\":[[2.0,2.0],[2.0,3.0],[3.0,2.0]]}]")))
                .respond(response()
                        .withStatusCode(200)
                        .withHeaders(
                                new Header("Content-Type", "application/json; charset=utf-8"))
                        .withBody(exact("{\"transformations\": [{\"rotation\": 0, \"scale\": 1, \"translation\": [2, 2]},{\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}, {\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}, {\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}, {\"rotation\": 0, \"scale\": 1, \"translation\": [0, 0]}, {\"rotation\": 0, \"scale\": 1, \"translation\": [2, 2]}]}"))
                        .withDelay(TimeUnit.MILLISECONDS, 500));
    }

    @Test
    public void testImmutabilityIdentification(@Autowired MockMvc mvc) throws Exception {
        mockCPDIdentification();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]]], \"metadata\": {\"timePeriod\": 2, \"startTime\": 1500}}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(post("/storyboard/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"parameters\": {\"immutability\": 5}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "[{\"events\":[{\"type\":\"IMMUTABILITY\",\"trigger\":{\"transformation\":6}}],\"temporalRange\":[1500,1506],\"phenomena\":[{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1500},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1502},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1504},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1506}]}," +
                                "{\"events\":[{\"type\":\"UNIMPORTANT\"}],\"temporalRange\":[1506,1508],\"phenomena\":[{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1506},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1508}]}]"));
    }

    @Test
    public void testImmutabilityComplexIdentification(@Autowired MockMvc mvc) throws Exception {
        mockCPDComplexIdentification();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[-2,-2],[-2,-1],[-1,-2]], [[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]], [[0,0],[0,1],[1,0]], [[2,2],[2,3],[3,2]]], \"metadata\": {\"timePeriod\": 2, \"startTime\": 1500}}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(post("/storyboard/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"parameters\": {\"immutability\": 5, \"translation\": {\"absoluteAcc\": 5.5}}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "[{\"events\":[{\"threshold\":\"ABSOLUTE_ACC\",\"type\":\"TRANSLATION\",\"trigger\":{\"transformation\":[2.0,2.0]}}],\"temporalRange\":[1500,1502],\"phenomena\":[{\"representation\":[[-2.0,-2.0],[-2.0,-1.0],[-1.0,-2.0]],\"timestamp\":1500},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1502}]}," +
                                "{\"events\":[{\"threshold\":\"ABSOLUTE_ACC\",\"type\":\"TRANSLATION\",\"trigger\":{\"transformation\":[0.0,0.0]}},{\"type\":\"IMMUTABILITY\",\"trigger\":{\"transformation\":6}}],\"temporalRange\":[1502,1508],\"phenomena\":[{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1502},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1504},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1506},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1508}]}," +
                                "{\"events\":[{\"threshold\":\"ABSOLUTE_ACC\",\"type\":\"TRANSLATION\",\"trigger\":{\"transformation\":[2.0,2.0]}}],\"temporalRange\":[1508,1512],\"phenomena\":[{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1508},{\"representation\":[[0.0,0.0],[0.0,1.0],[1.0,0.0]],\"timestamp\":1510},{\"representation\":[[2.0,2.0],[2.0,3.0],[3.0,2.0]],\"timestamp\":1512}]}]"
                ));
    }
}

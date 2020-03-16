package stfXCore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StoryboardCreationTests {

    @Autowired
    private Environment env;

    MockRestServiceServer mockServer;

    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(new RestTemplate());
    }

    @Test
    public void testStoryboardCreation(@Autowired MockMvc mvc) throws Exception {
        mockServer.expect(requestTo(env.getProperty("PSR_endpoint")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withSuccess("{\"rotation\": 0.00020695867664263134, \"scale\": 0.9943084085430711, \"translation\": [-0.26522648780481717, -0.2675864390243703]}", MediaType.APPLICATION_JSON));

        mvc.perform(post("/storyboard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));
    }


}

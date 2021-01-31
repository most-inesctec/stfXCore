package stfXCore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetEventsOfInterestTests extends MockTemplate {

    @Test
    public void testStoryboardNotFound(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        mvc.perform(post("/storyboard/200")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find storyboard 200"));
    }

    @Test
    public void testThresholdsMissingInput(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3, \"startTime\": 50, \"name\": \"DatasetName\"}}"))
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
    public void testGetEventsOfInterest(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3, \"startTime\": 50, \"name\": \"DatasetName\"}}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(post("/storyboard/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"parameters\": {\"immutability\": 5}}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEventsOfInterestNoise(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3, \"startTime\": 50, \"name\": \"DatasetName\"}}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(post("/storyboard/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"parameters\": {\"translation\": {\"delta\": 0.2, \"noiseEpsilon\": 0.4}}}"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "[{\"events\":[{\"type\":\"UNIMPORTANT\"}],\"temporalRange\":[50,53],\"phenomena\":[{\"representation\":[[49.448746,49.44956],[53.430676,49.449554],[53.434605,53.427555],[51.43714,55.40298],[49.443207,53.429226],[49.448746,49.44956]],\"timestamp\":50},{\"representation\":[[50.0,50.0],[54.0,50.0],[54.0,54.0],[52.0,56.0],[50.0,54.0],[50.0,50.0]],\"timestamp\":53}]}]"
                ));
    }
}

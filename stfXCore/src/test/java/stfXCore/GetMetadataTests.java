package stfXCore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetMetadataTests extends MockTemplate {

    @Test
    public void testGetMetadata(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        String id = mvc.perform(post("/storyboard")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3, \"startTime\": 50, \"name\": \"DatasetName\"}}"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(get("/storyboard/metadata/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"timePeriod\":3,\"startTime\":50,\"name\":\"DatasetName\"}"));
    }

    @Test
    public void testStoryboardNotFound(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/storyboard/metadata/10"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Could not find storyboard 10"));
    }
}

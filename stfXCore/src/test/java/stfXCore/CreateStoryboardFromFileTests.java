package stfXCore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateStoryboardFromFileTests extends MockTemplate {

    @Test
    public void testStoryboardCreation(@Autowired MockMvc mvc) throws Exception {
        mockCPD();
        MockMultipartFile testFile = new MockMultipartFile("dataset", "test.json", "aplication/json",
                "{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3, \"startTime\": 50, \"name\": \"DatasetName\"}}".getBytes());

        mvc.perform(multipart("/storyboard/file")
                .file(testFile))
                .andExpect(status().isOk());
    }

    @Test
    public void testStoryboardBadInput(@Autowired MockMvc mvc) throws Exception {
        MockMultipartFile missingDatasetFile = new MockMultipartFile("dataset", "test.json", "aplication/json",
                "{\"metadata\": {\"timePeriod\": 3}}".getBytes());
        MockMultipartFile missingMetadataFile = new MockMultipartFile("dataset", "test.json", "aplication/json",
                "{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]]}".getBytes());
        MockMultipartFile missingTimePeriodFile = new MockMultipartFile("dataset", "test.json", "aplication/json",
                "{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"startTime\": 50}}".getBytes());
        MockMultipartFile missingStartTimeFile = new MockMultipartFile("dataset", "test.json", "aplication/json",
                "{\"dataset\": [[[49.44874674652582, 49.44955690588778], [53.43067605954686, 49.44955600296501], [53.4346053725679, 53.427555100042234], [51.43713990926295, 55.402981930429604], [49.44320808526126, 53.42922779467332], [49.44874674652582, 49.44955690588778]], [[50.0, 50.0], [54.0, 50.0], [54.0, 54.0], [52.0, 56.0], [50.0, 54.0], [50.0, 50.0]]], \"metadata\": {\"timePeriod\": 3}}".getBytes());
        MockMultipartFile missingContent = new MockMultipartFile("dataset", "test.json", "aplication/json",
                "".getBytes());

        // Missing dataset
        mvc.perform(multipart("/storyboard/file")
                .file(missingDatasetFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));

        // Missing metadata
        mvc.perform(multipart("/storyboard/file")
                .file(missingMetadataFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));

        // Missing timeperiod in dataset
        mvc.perform(multipart("/storyboard/file")
                .file(missingTimePeriodFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));

        // Missing startTime in dataset
        mvc.perform(multipart("/storyboard/file")
                .file(missingStartTimeFile))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));

        // Missing content
        mvc.perform(multipart("/storyboard/file")
                .file(missingContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Missing information in the performed request"));
    }
}

package stfXCore.Models.Storyboard;

import lombok.Data;

import java.io.Serializable;

@Data
public class DatasetMetadata implements Serializable {

    private Long timePeriod;

    /**
     * Start time since 1 Jan 1970
     */
    private Long startTime;

    private String name;

    DatasetMetadata() {}
}

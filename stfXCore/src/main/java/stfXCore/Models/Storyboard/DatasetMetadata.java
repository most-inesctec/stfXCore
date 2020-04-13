package stfXCore.Models.Storyboard;

import lombok.Data;

import java.io.Serializable;

@Data
public class DatasetMetadata implements Serializable {

    private Float timePeriod;

    private String name;

    DatasetMetadata() {}
}

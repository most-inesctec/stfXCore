package stfXCore.Services.Transformations;

import lombok.Data;

import java.util.ArrayList;

@Data
public class TransformationList {

    private ArrayList<RigidTransformation> transformations;

    TransformationList() {}
}

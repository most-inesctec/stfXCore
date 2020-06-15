package stfXCore.Services;

import stfXCore.Models.Dataset;
import stfXCore.Models.SnapshotPair;

import java.util.ArrayList;

public class SnapshotsBuilder {

    public static ArrayList<SnapshotPair> createSnapshots(Dataset dataset) {
        ArrayList<SnapshotPair> snapshotPairs = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Float>>> representations = dataset.getDataset();
        Long timePeriod = dataset.getMetadata().getTimePeriod();
        Long startTime = dataset.getMetadata().getStartTime();

        for (int i = 0; i < representations.size() - 1; ++i)
            snapshotPairs.add(new SnapshotPair()
                    .setX(representations.get(i), startTime + timePeriod * i)
                    .setY(representations.get(i + 1), startTime + timePeriod * (i + 1)));

        return snapshotPairs;
    }
}

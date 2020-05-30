package stfXCore.Services;

import stfXCore.Models.Dataset;
import stfXCore.Models.Snapshot;

import java.util.ArrayList;

public class SnapshotsBuilder {

    public static ArrayList<Snapshot> createSnapshots(Dataset dataset) {
        ArrayList<Snapshot> snapshots = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Float>>> representations = dataset.getDataset();
        Long timePeriod = dataset.getMetadata().getTimePeriod();
        Long startTime = dataset.getMetadata().getStartTime();

        for (int i = 0; i < representations.size() - 1; ++i)
            snapshots.add(new Snapshot()
                    .setX(representations.get(i), startTime + timePeriod * i)
                    .setY(representations.get(i + 1), startTime + timePeriod * (i + 1)));

        return snapshots;
    }
}

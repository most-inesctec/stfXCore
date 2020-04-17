package stfXCore.Services;

import stfXCore.Models.Storyboard.Dataset;
import stfXCore.Models.Storyboard.Snapshot;

import java.util.ArrayList;

public class SnapshotsBuilder {

    public static ArrayList<Snapshot> createSnapshots(Dataset dataset) {
        ArrayList<Snapshot> snapshots = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Float>>> representations = dataset.getDataset();
        Long timePeriod = dataset.getMetadata().getTimePeriod();

        for (int i = 0; i < representations.size() - 1; ++i)
            snapshots.add(new Snapshot()
                    .setX(representations.get(i), timePeriod * i)
                    .setY(representations.get(i + 1), timePeriod * (i + 1)));

        return snapshots;
    }
}

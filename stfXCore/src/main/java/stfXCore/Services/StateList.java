package stfXCore.Services;

import stfXCore.Models.Snapshot;
import stfXCore.Models.Transformations.SnapshotTransformationPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StateList {

    private ArrayList<Snapshot> snapshots;

    public StateList(List<SnapshotTransformationPair> transformations) {
        this.snapshots = transformations.stream()
                .map(pair -> pair.getFirst().getX())
                .collect(Collectors.toCollection(ArrayList::new));
        // Adding last representation
        this.snapshots.add(transformations.get(transformations.size() - 1).getFirst().getY());
    }

    public ArrayList<Long> getTemporalRange() {
        return new ArrayList<Long>(Arrays.asList(
                snapshots.get(0).getTimestamp(),
                snapshots.get(snapshots.size() - 1).getTimestamp()
        ));
    }

    public ArrayList<Snapshot> getStates(Long lowerBound) {
        int lowerIndex = -1;
        for (int i = 0; i < snapshots.size(); ++i) {
            if (lowerBound.equals(snapshots.get(i).getTimestamp()))
                lowerIndex = i;
        }

        return new ArrayList<>(snapshots.subList(lowerIndex, snapshots.size()));
    }

    public ArrayList<Snapshot> getStates(Long lowerBound, Long upperBound) {
        int lowerIndex = -1;
        int upperIndex = -1;

        for (int i = 0; i < snapshots.size(); ++i) {
            if (lowerBound.equals(snapshots.get(i).getTimestamp()))
                lowerIndex = i;
            else if (upperBound.equals(snapshots.get(i).getTimestamp()))
                upperIndex = i + 1;
        }

        return new ArrayList<>(snapshots.subList(lowerIndex, upperIndex));
    }
}

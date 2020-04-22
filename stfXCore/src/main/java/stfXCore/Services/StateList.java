package stfXCore.Services;

import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.State;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class StateList {

    private ArrayList<State> states;

    public StateList(ArrayList<Pair<Snapshot, RigidTransformation>> transformations) {
        this.states = transformations.stream()
                .map(pair -> pair.getFirst().getX())
                .collect(Collectors.toCollection(ArrayList::new));
        // Adding last representation
        this.states.add(transformations.get(transformations.size() - 1).getFirst().getY());
    }

    public ArrayList<Long> getTemporalRange() {
        return new ArrayList<Long>(Arrays.asList(
                states.get(0).getTimestamp(),
                states.get(states.size() - 1).getTimestamp()
        ));
    }

    public ArrayList<State> getStates(Long lowerBound) {
        int lowerIndex = -1;
        for (int i = 0; i < states.size(); ++i) {
            if (lowerBound.equals(states.get(i).getTimestamp()))
                lowerIndex = i;
        }

        return new ArrayList<>(states.subList(lowerIndex, states.size()));
    }

    public ArrayList<State> getStates(Long lowerBound, Long upperBound) {
        int lowerIndex = -1;
        int upperIndex = -1;

        for (int i = 0; i < states.size(); ++i) {
            if (lowerBound.equals(states.get(i).getTimestamp()))
                lowerIndex = i;
            else if (upperBound.equals(states.get(i).getTimestamp()))
                upperIndex = i + 1;
        }

        return new ArrayList<>(states.subList(lowerIndex, upperIndex));
    }
}

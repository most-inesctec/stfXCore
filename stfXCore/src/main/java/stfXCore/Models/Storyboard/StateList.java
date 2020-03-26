package stfXCore.Models.Storyboard;

import stfXCore.Services.Transformations.RigidTransformation;
import stfXCore.Utils.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class StateList {

    private ArrayList<State> states;

    StateList(ArrayList<Pair<Snapshot, RigidTransformation>> transformations) {
        this.states = new ArrayList<State>(
                transformations.stream()
                        .map(pair -> pair.getFirst().getX())
                        .collect(Collectors.toList()));
        // Adding last representation
        this.states.add(transformations.get(transformations.size() - 1).getFirst().getY());
    }

    public ArrayList<State> getStates(Float lowerBound) {
        int lowerIndex = -1;
        for (int i = 0; i < states.size(); ++i) {
            if (lowerBound.equals(states.get(i).getTimestamp()))
                lowerIndex = i;
        }

        return new ArrayList<>(states.subList(lowerIndex, states.size()));
    }

    public ArrayList<State> getStates(Float lowerBound, Float upperBound) {
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

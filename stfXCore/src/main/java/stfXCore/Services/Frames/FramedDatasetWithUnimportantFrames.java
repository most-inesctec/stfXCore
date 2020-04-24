package stfXCore.Services.Frames;

import stfXCore.Services.StateList;
import stfXCore.Utils.Pair;

import java.util.ArrayList;

public class FramedDatasetWithUnimportantFrames implements IFramedDataset {

    private FramedDataset framedDataset;

    public FramedDatasetWithUnimportantFrames(FramedDataset framedDataset) {
        this.framedDataset = framedDataset;
    }

    private ArrayList<Frame> addUnimportantFrames(ArrayList<Frame> frames) {
        if (frames.size() == 0)
            return frames;

        StateList states = framedDataset.getStates();
        Pair<Long, Long> interval = framedDataset.getInterval();
        Long initialTimestamp = interval.getFirst();
        Long finalTimestamp = interval.getSecond();

        // First element case
        if (initialTimestamp < frames.get(0).lowerBound())
            frames.add(0,
                    new UnimportantFrame(states.getStates(initialTimestamp, frames.get(0).lowerBound())));

        // Middle elements
        for (int i = 1; i < frames.size(); ++i) {
            if (frames.get(i - 1).upperBound() < frames.get(i).lowerBound())
                frames.add(i,
                        new UnimportantFrame(states.getStates(frames.get(i - 1).upperBound(), frames.get(i).lowerBound())));
        }

        // Last element case
        if (finalTimestamp > frames.get(frames.size() - 1).upperBound())
            frames.add(frames.size(),
                    new UnimportantFrame(states.getStates(frames.get(frames.size() - 1).upperBound(), finalTimestamp)));

        return frames;
    }


    @Override
    public ArrayList<Frame> getFrames() {
        return addUnimportantFrames(framedDataset.getFrames());
    }
}

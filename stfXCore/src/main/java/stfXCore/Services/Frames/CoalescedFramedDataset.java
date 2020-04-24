package stfXCore.Services.Frames;

import java.util.ArrayList;

public class CoalescedFramedDataset implements IFramedDataset {

    private IFramedDataset framedDataset;

    public CoalescedFramedDataset(IFramedDataset framedDataset) {
        this.framedDataset = framedDataset;
    }

    private ArrayList<Frame> coalesceFrames(ArrayList<Frame> frames) {
        if (frames.size() <= 1)
            return frames;

        ArrayList<Frame> coalescedFrames = new ArrayList<>();
        Frame processFrame = frames.get(0);

        for (int i = 1; i < frames.size(); ++i) {
            Frame jointFrame = processFrame.joinFrames(frames.get(i));
            if (jointFrame == null) {
                coalescedFrames.add(processFrame);
                processFrame = frames.get(i);
            } else {
                processFrame = jointFrame;
            }
        }

        return coalescedFrames;
    }

    @Override
    public ArrayList<Frame> getFrames() {
        return coalesceFrames(framedDataset.getFrames());
    }
}

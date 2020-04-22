package stfXCore.Services.Parsers;

import stfXCore.Models.Storyboard.Snapshot;
import stfXCore.Models.Storyboard.Transformations.RigidTransformation;
import stfXCore.Services.DataTypes.LongTransformation;
import stfXCore.Services.Events.Event;
import stfXCore.Utils.Pair;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static stfXCore.Services.Events.Event.Transformation.IMMUTABILITY;

public class ImmutabilityParser implements ITransformationParser {

    private Long threshold;

    Long counter;

    ArrayList<Pair<Long, LongTransformation>> representations;

    ImmutabilityParser(Long threshold) {
        this.threshold = threshold;
    }

    private void resetCounters() {
        counter = 0L;
        representations = new ArrayList<>();
    }

    protected ArrayList<Event<?>> filterThreshold(List<Pair<Snapshot, Boolean>> transformations) {
        ArrayList<Event<?>> eventsOfInterest = new ArrayList<>();
        resetCounters();

        if (threshold != null) {
            for (Pair<Snapshot, Boolean> transformation : transformations) {
                if (transformation.getSecond()) {
                    Snapshot s = transformation.getFirst();

                    if (representations.size() == 0)
                        representations.add(new Pair<Long, LongTransformation>(
                                s.getX().getTimestamp(),
                                new LongTransformation(0L)));

                    counter += s.getY().getTimestamp() - s.getX().getTimestamp();
                    representations.add(new Pair<Long, LongTransformation>(
                            s.getY().getTimestamp(),
                            new LongTransformation(counter)));
                } else {
                    resetCounters();
                    continue;
                }

                if (counter >= threshold) {
                    eventsOfInterest.add(
                            new Event<LongTransformation>(IMMUTABILITY)
                                    .setValues(representations));
                    resetCounters();
                }
            }
        }

        return eventsOfInterest;
    }

    @Override
    public ArrayList<Event<?>> parse(@NotNull ArrayList<Pair<Snapshot, RigidTransformation>> rigidTransformations) {
        return filterThreshold(
                rigidTransformations.stream().map(
                        pair -> new Pair<>(pair.getFirst(), pair.getSecond().isNull()))
                        .collect(Collectors.toList()));
    }
}

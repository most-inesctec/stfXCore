package stfXCore.Services.Parsers;

import stfXCore.Models.Storyboard.Transformations.SnapshotTransformationPair;
import stfXCore.Services.Events.Event;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public interface ITransformationParser {

    ArrayList<Event<?>> parse(@NotNull List<SnapshotTransformationPair> rigidTransformations);
}

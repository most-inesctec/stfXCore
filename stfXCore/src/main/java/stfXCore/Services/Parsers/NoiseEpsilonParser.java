package stfXCore.Services.Parsers;

import stfXCore.Models.Snapshot;
import stfXCore.Models.Thresholds.GenericThreshold;
import stfXCore.Services.DataTypes.TransformationDataType;
import stfXCore.Utils.Pair;

import java.util.ArrayList;
import java.util.List;

public class NoiseEpsilonParser<T extends TransformationDataType> {

    private final static float PERCENTAGE_AS_ERROR = 0.01f;

    protected GenericThreshold<Float> threshold;

    NoiseEpsilonParser(GenericThreshold<Float> threshold) {
        this.threshold = threshold;
    }

    public List<Pair<Snapshot, T>> filterNoise(List<Pair<Snapshot, T>> transformations) {
        Float noiseEpsilon = threshold.getNoiseEpsilon();
        if (noiseEpsilon == null)
            return transformations;

        List<Pair<Snapshot, T>> filtered = new ArrayList<>();
        Float deltaEpsilon = threshold.getDelta() == null ? 0f : threshold.getDelta() * PERCENTAGE_AS_ERROR;
        float filter = deltaEpsilon < noiseEpsilon ? noiseEpsilon : deltaEpsilon;

        for (Pair<Snapshot, T> transformation : transformations) {
            T transformationValue = transformation.getSecond();
            filtered.add(Math.abs(transformationValue.numericalValue()) < filter ?
                    new Pair<>(transformation.getFirst(), (T) transformationValue.nullValue()) :
                    transformation);
        }
        return filtered;
    }
}

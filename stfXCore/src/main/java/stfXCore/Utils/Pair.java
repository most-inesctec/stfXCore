package stfXCore.Utils;

import java.io.Serializable;

public class Pair<K, V> implements Serializable {

    private K first;

    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return this.first;
    }

    public V getSecond() {
        return this.second;
    }
}

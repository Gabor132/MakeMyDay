package Auxiliars;

import java.util.Map;

public class Pair<T, V> implements Map.Entry {

    private T key;
    private V value;

    public Pair(T key, V value){
        this.key = key;
        this.value = value;
    }


    @Override
    public T getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
        this.value = (V) value;
        return this.value;
    }
}

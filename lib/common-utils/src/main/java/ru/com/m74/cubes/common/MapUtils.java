package ru.com.m74.cubes.common;

import java.util.HashMap;

public class MapUtils<K, V> extends HashMap<K, V> {

    private MapUtils(K key, V val) {
        add(key, val);
    }

    private MapUtils() {
    }

    public MapUtils<K, V> add(K key, V val) {
        put(key, val);
        return this;
    }

    public static MapUtils<String, Object> map() {
        return new MapUtils<>();
    }

    public static MapUtils<String, Object> map(String key, Object val) {
        return new MapUtils<>(key, val);
    }

}
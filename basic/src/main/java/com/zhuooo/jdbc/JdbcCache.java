package com.zhuooo.jdbc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcCache {

    private JdbcCache() {

    }

    private static Map map = new ConcurrentHashMap();

    public static <T> void put(String key, T value) {
        map.put(key, value);
    }

    public static <T> T get(String key) {
        return (T) map.get(key);
    }

    public static int size() {
        return map.size();
    }
}

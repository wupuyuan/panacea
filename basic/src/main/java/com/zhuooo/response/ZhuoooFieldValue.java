package com.zhuooo.response;

import java.io.Serializable;

public class ZhuoooFieldValue<K, V> implements Serializable {
    public ZhuoooFieldValue(K parament, V argument) {
        this.parament = parament;
        this.argument = argument;
    }

    public ZhuoooFieldValue() {

    }

    /**
     * 形参
     */
    K parament;

    /**
     * 实参
     */
    V argument;

    public K getParament() {
        return parament;
    }

    public void setParament(K parament) {
        this.parament = parament;
    }

    public V getArgument() {
        return argument;
    }

    public void setArgument(V argument) {
        this.argument = argument;
    }
}

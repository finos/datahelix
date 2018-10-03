package com.scottlogic.deg.generator.utils;

public class MutableInt{
    private int value;

    public MutableInt(int value){
        this.value = value;
    }

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}

package com.scottlogic.deg.generator;

public class Field {
    public final String name;

    public Field(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

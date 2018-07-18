package com.scottlogic.deg.generator;

import java.util.List;

public class Field
{
    public final String name;
    public final String type;
    public final List<IConstraint> constraints;

    public Field(String name, String type, List<IConstraint> constraints) {
        this.name = name;
        this.type = type;
        this.constraints = constraints;
    }
}

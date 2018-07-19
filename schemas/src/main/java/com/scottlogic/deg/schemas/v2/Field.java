package com.scottlogic.deg.schemas.v2;

import com.scottlogic.deg.schemas.v2.constraints.Constraint;

import java.util.List;

public class Field {
    public String name;
    public Format format;
    public List<Constraint> constraints;
}

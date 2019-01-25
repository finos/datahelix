package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.generator.constraints.Constraint;

import java.util.Iterator;
import java.util.stream.Stream;

public interface ViolationFilter {
    boolean accept(Constraint constraint);
}
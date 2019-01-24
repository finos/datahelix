package com.scottlogic.deg.generator.violations.filters;

import com.scottlogic.deg.generator.constraints.Constraint;

import java.util.Iterator;
import java.util.stream.Stream;

public interface ViolationFilter {
    boolean accept(Constraint constraint);

    static Stream<Constraint> applyFilters(Stream<Constraint> constraints, Iterator<ViolationFilter> filters){
        if (!filters.hasNext()) { return constraints; }
        return applyFilters(constraints.filter(filters.next()::accept), filters);
    }
}
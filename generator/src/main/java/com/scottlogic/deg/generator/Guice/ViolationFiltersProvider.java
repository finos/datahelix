package com.scottlogic.deg.generator.Guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.generator.violations.filters.ConstraintTypeViolationFilter;
import com.scottlogic.deg.generator.violations.filters.ViolationFilter;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ViolationFiltersProvider implements Provider<List<ViolationFilter>> {
    private final GenerationConfigSource commandLine;
    private final AtomicConstraintTypeMapper mapper;

    @Inject
    public ViolationFiltersProvider(GenerationConfigSource commandLine, AtomicConstraintTypeMapper mapper) {
        this.commandLine = commandLine;
        this.mapper = mapper;
    }

    @Override
    public List<ViolationFilter> get() {
        if (commandLine.getConstraintsToNotViolate() == null){
            return Collections.emptyList();
        }

        return commandLine.getConstraintsToNotViolate().stream()
            .map(mapper::toConstraintClass)
            .map(ConstraintTypeViolationFilter::new)
            .collect(Collectors.toList());
    }

}

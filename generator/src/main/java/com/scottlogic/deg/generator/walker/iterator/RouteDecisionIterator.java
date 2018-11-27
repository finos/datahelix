package com.scottlogic.deg.generator.walker.iterator;

import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteDecisionIterator implements DecisionIterator {

    private final DecisionIterator nextDecision;
    private final Collection<ConstraintIterator> optionsCache;

    private Iterator<ConstraintIterator> options;
    private ConstraintIterator currentOption;
    private RowSpecRoute currentOptionsSubroute;

    RouteDecisionIterator(Collection<ConstraintIterator> optionsCache, DecisionIterator nextDecision){
        this.nextDecision = nextDecision;
        this.optionsCache = optionsCache;

        this.options = this.optionsCache.iterator();
        this.currentOption = this.options.next();
        this.currentOptionsSubroute = this.currentOption.next();
    }

    @Override
    public boolean hasNext() {
        return options.hasNext()|| nextDecision.hasNext() || currentOption.hasNext();
    }

    @Override
    public Collection<RowSpecRoute> next() {
        if (!nextDecision.hasNext()) {
            nextDecision.reset();

            if (!currentOption.hasNext()) {
                currentOption = options.next();
            }

            currentOptionsSubroute = currentOption.next();
        }

        return Stream.concat(
            Stream.of(currentOptionsSubroute),
            nextDecision.next().stream())
            .collect(Collectors.toSet());
    }

    @Override
    public void reset(){
        nextDecision.reset();
        for (ConstraintIterator option: optionsCache) {
            option.reset();
        }

        options = optionsCache.iterator();
        currentOption = options.next();
        currentOptionsSubroute = currentOption.next();
    }
}

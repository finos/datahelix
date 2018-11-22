package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteDecisionIterator implements DecisionIterator {

    private DecisionIterator nextDecision;
    private Collection<ConstraintIterator> optionsCache;
    private Iterator<ConstraintIterator> options;
    private ConstraintIterator currentOption;
    private RowSpecRoute currentOptionsSubroute;

    public RouteDecisionIterator(Collection<ConstraintIterator> options, DecisionIterator nextDecision){
        this.optionsCache = options;
        this.options = this.optionsCache.iterator();
        this.currentOption = this.options.next();
        this.nextDecision = nextDecision;
    }

    @Override
    public boolean hasNext() {
        return options.hasNext()|| nextDecision.hasNext() || currentOption.hasNext();
    }

    @Override
    public Collection<RowSpecRoute> next() {
        if (currentOptionsSubroute == null){
            currentOptionsSubroute = currentOption.next();
        }

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
        currentOptionsSubroute = null;
    }
}

package com.scottlogic.deg.generator.walker;

import com.google.common.collect.ImmutableSet;
import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.factory.DecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;

public class RouteDecisionIterator implements DecisionIterator {

    private DecisionIterator nextDecision;
    private List<ConstraintIterator> options;
    private int currentOption;
    private RowSpecRoute currentOptionsSubroute;

    public RouteDecisionIterator(List<ConstraintIterator> options, DecisionIterator nextDecision){
        this.options = options;
        this.nextDecision = nextDecision;
    }

    @Override
    public boolean hasNext() {
        return currentOption < options.size()-1 || nextDecision.hasNext()
            || (currentOption == options.size()-1 && options.get(currentOption).hasNext());
    }

    @Override
    public Collection<RowSpecRoute> next() {
        if (currentOptionsSubroute == null){
            currentOptionsSubroute = getCurrentOptionsIterator().next();
        }

        if (!nextDecision.hasNext()) {
            nextDecision.reset();

            if (!getCurrentOptionsIterator().hasNext()) {
                currentOption++;
            }
            currentOptionsSubroute = getCurrentOptionsIterator().next();
        }
        return new ImmutableSet.Builder<RowSpecRoute>()
            .addAll(nextDecision.next())
            .add(currentOptionsSubroute).build();
    }

    @Override
    public void reset(){
        nextDecision.reset();
        for (ConstraintIterator option: options) {
            option.reset();
        }
        currentOption = 0;
        currentOptionsSubroute = null;
    }

    private ConstraintIterator getCurrentOptionsIterator() { return options.get(currentOption); }
}

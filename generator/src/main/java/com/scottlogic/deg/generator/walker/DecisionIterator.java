package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.builder.IConstraintIterator;
import com.scottlogic.deg.generator.walker.builder.IDecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;

public class DecisionIterator implements IDecisionIterator {

    private IDecisionIterator nextDecision;
    private List<IConstraintIterator> options;
    private int currentOption;
    private RowSpecRoute currentOptionsSubroute;

    public DecisionIterator(List<IConstraintIterator> options, IDecisionIterator nextDecision){
        this.options = options;
        this.nextDecision = nextDecision;
    }

    @Override
    public boolean hasNext() {
        return currentOption < options.size()-1 || nextDecision.hasNext();
    }

    @Override
    public List<RowSpecRoute> next() {
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

        List<RowSpecRoute> sideRoutes;
        sideRoutes = nextDecision.next();
        sideRoutes.add(0, currentOptionsSubroute);
        return sideRoutes;
    }

    @Override
    public void reset(){
        nextDecision.reset();
        for (IConstraintIterator option: options) {
            option.reset();
        }
        currentOption = 0;
        currentOptionsSubroute = null;
    }

    private IConstraintIterator getCurrentOptionsIterator() { return options.get(currentOption); }
}

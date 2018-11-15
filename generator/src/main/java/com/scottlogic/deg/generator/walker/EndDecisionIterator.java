package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.factory.ConstraintIterator;
import com.scottlogic.deg.generator.walker.factory.DecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;

public class EndDecisionIterator implements DecisionIterator {
    private List<ConstraintIterator> options;
    private int currentOption;

    public EndDecisionIterator(List<ConstraintIterator> options){
        this.options = options;
    }

    @Override
    public boolean hasNext() {
        return currentOption < options.size();
    }

    @Override
    public List<RowSpecRoute> next() {
        ConstraintIterator currentOptionIterator = options.get(currentOption);
        RowSpecRoute nextOption = currentOptionIterator.next();

        if (!currentOptionIterator.hasNext()){
            currentOption++;
        }
        return new ArrayList<>(Arrays.asList(nextOption));
    }

    @Override
    public void reset(){
        currentOption = 0;
        for (ConstraintIterator option: options) {
            option.reset();
        }
    }
}

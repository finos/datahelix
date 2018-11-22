package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;

public class EndDecisionIterator implements DecisionIterator {
    private Collection<ConstraintIterator> optionsCache;
    private Iterator<ConstraintIterator> options;
    private ConstraintIterator currentOption;

    public EndDecisionIterator(Collection<ConstraintIterator> options){
        this.optionsCache = options;
        this.options = optionsCache.iterator();
        this.currentOption = this.options.next();
    }

    @Override
    public boolean hasNext() {
        return options.hasNext() || currentOption.hasNext();
    }

    @Override
    public Collection<RowSpecRoute> next() {
        if (!currentOption.hasNext()){
            currentOption = options.next();
        }

        return Collections.singleton(currentOption.next());
    }

    @Override
    public void reset(){
        options = optionsCache.iterator();
        currentOption = options.next();
        for (ConstraintIterator option: optionsCache) {
            option.reset();
        }
    }
}

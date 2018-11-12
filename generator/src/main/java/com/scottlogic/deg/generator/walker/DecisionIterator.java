package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.builder.ConstraintBuilder;
import com.scottlogic.deg.generator.walker.builder.DecisionBuilder;
import com.scottlogic.deg.generator.walker.builder.IConstraintIterator;
import com.scottlogic.deg.generator.walker.builder.IDecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;

//consider making lasIterator implementation for final decision in a list
public class DecisionIterator implements IDecisionIterator {

    private IDecisionIterator nextIterator;
    private List<IConstraintIterator> options = new ArrayList<>();
    private int currentOption;
    private RowSpecRoute currentOptionsSubroute;

    public DecisionIterator(Queue<DecisionNode> decisionNodes){
        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.remove().getOptions()) {
            options.add(ConstraintBuilder.build(constraintNode, count));
            count++;
        }        
        nextIterator = DecisionBuilder.build(decisionNodes);
    }

    @Override
    public boolean hasNext() {
        return currentOption < options.size() -1 || nextIterator.hasNext();
    }

    @Override
    public List<RowSpecRoute> next() {
        List<RowSpecRoute> sideRoutes;
        if (nextIterator.hasNext()){
            sideRoutes = nextIterator.next();
            if (currentOptionsSubroute == null){
                currentOptionsSubroute = options.get(currentOption).next();
            }
            sideRoutes.add(0, currentOptionsSubroute);
            return sideRoutes;
        }

        IConstraintIterator currentOptionIterator = options.get(currentOption);
        if (!currentOptionIterator.hasNext()){
            currentOption++;
            currentOptionIterator = options.get(currentOption);
        }
        currentOptionsSubroute = currentOptionIterator.next();

        nextIterator.reset();
        sideRoutes = nextIterator.next();
        sideRoutes.add(0, currentOptionsSubroute);
        return sideRoutes;
    }

    @Override
    public void reset(){
        currentOption = 0;
        currentOptionsSubroute = null;
        nextIterator.reset();
        for (IConstraintIterator option: options) {
            option.reset();
        }
    }
}

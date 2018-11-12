package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.walker.builder.ConstraintBuilder;
import com.scottlogic.deg.generator.walker.builder.DecisionBuilder;
import com.scottlogic.deg.generator.walker.builder.IConstraintIterator;
import com.scottlogic.deg.generator.walker.builder.IDecisionIterator;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;

import java.util.*;

public class DecisionIterator implements IDecisionIterator {

    private IDecisionIterator nextDecision;
    private List<IConstraintIterator> options = new ArrayList<>();
    private int currentOption;
    private RowSpecRoute currentOptionsSubroute;

    private IConstraintIterator currentOptionIterator() { return options.get(currentOption); }

    public DecisionIterator(Queue<DecisionNode> decisionNodes){
        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.remove().getOptions()) {
            options.add(ConstraintBuilder.build(constraintNode, count));
            count++;
        }
        currentOptionsSubroute = currentOptionIterator().next();

        nextDecision = DecisionBuilder.build(decisionNodes);
    }

    @Override
    public boolean hasNext() {
        return currentOption < options.size()-1 || nextDecision.hasNext();
    }

    @Override
    public List<RowSpecRoute> next() {
        if (!nextDecision.hasNext()) {
            nextDecision.reset();

            if (!currentOptionIterator().hasNext()) {
                currentOption++;
            }
            currentOptionsSubroute = currentOptionIterator().next();
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
        currentOptionsSubroute = currentOptionIterator().next();
    }
}

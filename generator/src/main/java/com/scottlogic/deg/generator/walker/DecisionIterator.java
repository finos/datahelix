package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

//consider making lasIterator implementation for final decision in a list
public class DecisionIterator implements IDecisionIterator {

    private IDecisionIterator nextIterator;
    private List<IConstraintIterator> options = new ArrayList<>();
    private int currentOption;
    private RowSpecRoute currentOptionsSubroute;

    public static IDecisionIterator build(Collection<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty())
            return null;

        if(decisionNodes.size() == 1) 
            return new EndDecisionIterator(new ArrayList<>(decisionNodes).get(0));

        return new DecisionIterator(new LinkedList<>(decisionNodes));
    }

    private DecisionIterator(Queue<DecisionNode> decisionNodes){
        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.remove().getOptions()) {
            options.add(ConstraintIterator.build(constraintNode, count));
            count++;
        }        
        nextIterator = build(decisionNodes);        
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

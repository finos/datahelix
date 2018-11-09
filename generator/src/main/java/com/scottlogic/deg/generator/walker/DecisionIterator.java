package com.scottlogic.deg.generator.walker;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.routes.RowSpecRoute;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class DecisionIterator implements Iterator<List<RowSpecRoute>> {

    private DecisionIterator nextIterator;
    private List<ConstraintIterator> options = new ArrayList<>();
    private int currentOption;
    private RowSpecRoute currentOptionsSubroute;

    public static DecisionIterator build(Collection<DecisionNode> decisionNodes){
        if (decisionNodes == null || decisionNodes.isEmpty()) return null;

        return new DecisionIterator(new LinkedList<>(decisionNodes));
    }

    private DecisionIterator(Queue<DecisionNode> decisionNodes){
        if (decisionNodes.isEmpty()) { throw new IllegalArgumentException(); }

        int count = 0;
        for (ConstraintNode constraintNode: decisionNodes.remove().getOptions()) {
            options.add(new ConstraintIterator(constraintNode, count));
            count++;
        }

        if (decisionNodes.isEmpty()) {
            nextIterator = null; // maybe replace with optional
        } else {
            nextIterator = new DecisionIterator(decisionNodes);
        }
    }

    @Override
    public boolean hasNext() {
        return currentOption < options.size() || (nextIterator != null && nextIterator.hasNext());
    }

    @Override
    public List<RowSpecRoute> next() {//TODO refactor
        if (nextIterator == null) {
            currentOptionsSubroute = options.get(currentOption).next();
            currentOption++;
            List<RowSpecRoute> r = new ArrayList<>();
            r.add(0, currentOptionsSubroute);
            return r;
        }

        List<RowSpecRoute> sideRoutes;
        if (nextIterator.hasNext()){
            sideRoutes = nextIterator.next();
            sideRoutes.add(0, currentOptionsSubroute);
        }
        else {
            nextIterator.reset();
            currentOptionsSubroute = options.get(currentOption).next();
            currentOption++;

            sideRoutes = nextIterator.next();
            sideRoutes.add(0, currentOptionsSubroute);

        }

        return sideRoutes;
    }

    void reset(){
        currentOption = 0;
        currentOptionsSubroute = null;
    }
}

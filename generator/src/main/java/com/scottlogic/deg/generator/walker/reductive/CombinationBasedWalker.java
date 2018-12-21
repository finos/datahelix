package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.combination.Combination;
import com.scottlogic.deg.generator.generation.combination.CombinationProducer;
import com.scottlogic.deg.generator.fieldspecs.RowSpec;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.ReductiveDecisionTreeWalker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Decision tree walker that will use fixed combinations of values as seeds to the Reductive Decision Tree Walker.
 * More importantly, it only produces a single row for each combination before moving on.
 */
public class CombinationBasedWalker implements DecisionTreeWalker {

    private final CombinationProducer combinationProducer;
    private final ReductiveDecisionTreeWalker reductiveWalker;
    private final static int MAX_ROWSPEC_PER_COMBINATION = 1;

    public CombinationBasedWalker(CombinationProducer combinationProducer, ReductiveDecisionTreeWalker reductiveWalker){
        this.combinationProducer = combinationProducer;
        this.reductiveWalker = reductiveWalker;
    }

    @Override
    public Stream<RowSpec> walk(DecisionTree tree) {
        Stream<Combination> combinations = combinationProducer.getCombinations(tree).distinct(); // discard duplicate combinations
        return FlatMappingSpliterator.flatMap(
            combinations.map(combo -> {
                Deque<FixedField> initialFixFields = combo.getCombinations().entrySet().stream()
                    .map(entry ->
                        new FixedField(entry.getKey(), Stream.of(entry.getValue().getValue()), entry.getValue().getSource(), reductiveWalker.getMonitor()))
                    .peek(ff -> {
                        ff.getStream().iterator().next();
                    })
                    .collect(Collectors.toCollection(ArrayDeque::new));
                return new ReductiveState(tree.fields).with(initialFixFields);
            }),
            (ReductiveState initialState) -> this.reductiveWalker.walk(tree, initialState).limit(MAX_ROWSPEC_PER_COMBINATION));
    }
}

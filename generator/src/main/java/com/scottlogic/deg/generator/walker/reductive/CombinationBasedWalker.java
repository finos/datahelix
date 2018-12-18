package com.scottlogic.deg.generator.walker.reductive;

import com.scottlogic.deg.generator.FlatMappingSpliterator;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.generation.combination.Combination;
import com.scottlogic.deg.generator.generation.combination.CombinationProducer;
import com.scottlogic.deg.generator.restrictions.RowSpec;
import com.scottlogic.deg.generator.walker.DecisionTreeWalker;
import com.scottlogic.deg.generator.walker.ReductiveDecisionTreeWalker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombinationBasedWalker implements DecisionTreeWalker {

    private final CombinationProducer combinationProducer;
    private final FieldCollectionFactory fieldCollectionFactory;
    private final ReductiveDecisionTreeWalker reductiveWalker;

    public CombinationBasedWalker(CombinationProducer combinationProducer, FieldCollectionFactory fieldCollectionFactory, ReductiveDecisionTreeWalker reductiveWalker){
        this.combinationProducer = combinationProducer;
        this.fieldCollectionFactory = fieldCollectionFactory;
        this.reductiveWalker = reductiveWalker;
    }

    @Override
    public Stream<RowSpec> walk(DecisionTree tree) {
        Stream<Combination> combinations = combinationProducer.getCombinations()
            .distinct();

        return FlatMappingSpliterator.flatMap(
            combinations.map(combo -> {
                Deque<FixedField> initialFixFields = combo.getCombinations().entrySet().stream()
                    .map(entry -> new FixedField(entry.getKey(), Stream.of(entry.getValue().getValue()), entry.getValue().getSource(), reductiveWalker.getMonitor()))
                    .peek(ff -> {
                        ff.getStream().iterator().next();
                    })
                    .collect(Collectors.toCollection(ArrayDeque::new));
                return fieldCollectionFactory.create(tree, initialFixFields);
            }),
            fieldCollection -> this.reductiveWalker.walk(tree, fieldCollection).limit(1));
    }
}

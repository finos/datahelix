package com.scottlogic.deg.generator.decisiontree;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.RuleInformation;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public class MaxStringLengthInjectingDecisionTreeFactory implements DecisionTreeFactory{
    private final int maxLength;
    private final ProfileDecisionTreeFactory underlyingFactory;

    @Inject
    public MaxStringLengthInjectingDecisionTreeFactory(ProfileDecisionTreeFactory underlyingFactory) {
        this(underlyingFactory, GenerationConfig.Constants.MAX_STRING_LENGTH.intValue());
    }

    public MaxStringLengthInjectingDecisionTreeFactory(ProfileDecisionTreeFactory underlyingFactory, int maxLength) {
        this.underlyingFactory = underlyingFactory;
        this.maxLength = maxLength;
    }

    @Override
    public DecisionTreeCollection analyse(Profile profile) {
        DecisionTreeCollection collection = underlyingFactory.analyse(profile);
        return new DecisionTreeCollection(
            collection.getFields(),
            collection.getDecisionTrees()
                .stream()
                .map(this::injectConstraints)
                .collect(Collectors.toList())
        );
    }

    private DecisionTree injectConstraints(DecisionTree tree) {
        Set<RuleInformation> rules = Collections.emptySet(); //TODO: inject some detail here?

        return new DecisionTree(
            tree.rootNode.addAtomicConstraints(
                tree.fields
                    .stream()
                    .map(field -> new IsStringShorterThanConstraint(field, maxLength + 1, rules))
                    .collect(Collectors.toList())
            ),
            tree.fields,
            tree.description
        );
    }
}

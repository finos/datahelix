package com.scottlogic.deg.generator.decisiontree;

import com.google.inject.Inject;
import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.constraints.atomic.IsStringShorterThanConstraint;
import com.scottlogic.deg.generator.generation.GenerationConfig;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.schemas.v0_1.RuleDTO;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Decorator over a DecisionTreeFactory to inject a &lt;shorterThan X&gt; constraint at the root node for every field
 */
public class MaxStringLengthInjectingDecisionTreeFactory implements DecisionTreeFactory{
    private final int maxLength;
    private final DecisionTreeFactory underlyingFactory;

    @Inject
    public MaxStringLengthInjectingDecisionTreeFactory(ProfileDecisionTreeFactory underlyingFactory) {
        this(underlyingFactory, GenerationConfig.Constants.MAX_STRING_LENGTH);
    }

    public MaxStringLengthInjectingDecisionTreeFactory(DecisionTreeFactory underlyingFactory, int maxLength) {
        this.underlyingFactory = underlyingFactory;
        this.maxLength = maxLength;
    }

    @Override
    public DecisionTree analyse(Profile profile) {
        DecisionTree tree = underlyingFactory.analyse(profile);

        Set<RuleInformation> rules = Collections.singleton(createRule());

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

    private RuleInformation createRule() {
        RuleDTO ruleDTO = new RuleDTO();
        ruleDTO.rule = "Auto-injected: String-max-length";

        return new RuleInformation(ruleDTO);
    }
}

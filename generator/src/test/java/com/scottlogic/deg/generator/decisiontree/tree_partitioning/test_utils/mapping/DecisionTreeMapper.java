package com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.mapping;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.ConstraintNode;
import com.scottlogic.deg.generator.decisiontree.DecisionNode;
import com.scottlogic.deg.generator.decisiontree.DecisionTree;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.ConstraintNodeDto;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.DecisionNodeDto;
import com.scottlogic.deg.generator.decisiontree.tree_partitioning.test_utils.DecisionTreeDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionTreeMapper {
    public DecisionTree map(DecisionTreeDto decisionTreeDto) {
        return new DecisionTree(dtoToConstraintNode(decisionTreeDto.rootNode), getMappedProfileFields(decisionTreeDto));
    }

    private ProfileFields getMappedProfileFields(DecisionTreeDto decisionTreeDto) {
        final List<Field> mappedFields = decisionTreeDto.fields.fields
                .stream()
                .map(f -> new Field(f.name))
                .collect(Collectors.toList());

        return new ProfileFields(mappedFields);
    }

    private ConstraintNode dtoToConstraintNode(ConstraintNodeDto constraintNodeDto) {
        if (constraintNodeDto.decisions == null) {
            // Base case when no more decisions on a constraint node
            return new ConstraintNode(getAtomicConstraints(constraintNodeDto), Collections.emptyList());
        }

        List<DecisionNode> nodes = constraintNodeDto.decisions.stream()
                .map(this::dtoToDecisionNode).collect(Collectors.toList());

        return new ConstraintNode(getAtomicConstraints(constraintNodeDto), nodes);
    }

    private DecisionNode dtoToDecisionNode(DecisionNodeDto decisionNodeDto){
        Collection<ConstraintNode> options = decisionNodeDto.options.stream()
                .map(this::dtoToConstraintNode)
                .collect(Collectors.toList());

        return new DecisionNode(options);
    }

    private List<IConstraint> getAtomicConstraints(ConstraintNodeDto constraintNodeDto){
        return constraintNodeDto.atomicConstraints
                    .stream()
                    .map(IConstraintMapper::map)
                    .collect(Collectors.toList());
    }
}

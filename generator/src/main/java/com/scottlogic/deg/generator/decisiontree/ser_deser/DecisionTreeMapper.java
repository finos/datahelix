package com.scottlogic.deg.generator.decisiontree.ser_deser;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.decisiontree.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DecisionTreeMapper {
    public DecisionTree fromDto(DecisionTreeDto decisionTreeDto) {
        return new DecisionTree(
            dtoToConstraintNode(decisionTreeDto.rootNode),
            getMappedProfileFields(decisionTreeDto),
            decisionTreeDto.description);
    }
    
    public DecisionTreeDto toDto(DecisionTree tree) {
        DecisionTreeDto dto = new DecisionTreeDto();
        dto.rootNode = toDto(tree.getRootNode());
        return null;
    }
    
    private ConstraintNodeDto toDto(ConstraintNode node) {
        if (node.getDecisions()==null) {
            ConstraintNodeDto dto = new ConstraintNodeDto();
            dto.atomicConstraints = getConstraintsDto(node);
            return dto;
        }
        return null;
    }

    private ProfileFields getMappedProfileFields(DecisionTreeDto decisionTreeDto) {
        final List<Field> mappedFields = decisionTreeDto.fields
                .stream()
                .map(f -> new Field(f.name))
                .collect(Collectors.toList());

        return new ProfileFields(mappedFields);
    }

    private ConstraintNode dtoToConstraintNode(ConstraintNodeDto constraintNodeDto) {
        if (constraintNodeDto.decisions == null) {
            // Base case when no more decisions on a constraint node
            return new TreeConstraintNode(getAtomicConstraints(constraintNodeDto), Collections.emptyList());
        }

        List<DecisionNode> nodes = constraintNodeDto.decisions.stream()
                .map(this::dtoToDecisionNode).collect(Collectors.toList());

        return new TreeConstraintNode(getAtomicConstraints(constraintNodeDto), nodes);
    }

    private DecisionNode dtoToDecisionNode(DecisionNodeDto decisionNodeDto){
        Collection<ConstraintNode> options = decisionNodeDto.options.stream()
                .map(this::dtoToConstraintNode)
                .collect(Collectors.toList());

        return new TreeDecisionNode(options);
    }

    private List<IConstraint> getAtomicConstraints(ConstraintNodeDto constraintNodeDto){
        return constraintNodeDto.atomicConstraints
                    .stream()
                    .map(IConstraintMapper::map)
                    .collect(Collectors.toList());
    }
    
    private List<ConstraintDto> getConstraintsDto(ConstraintNode constraint) {
        return null; // FIXME
    }
}

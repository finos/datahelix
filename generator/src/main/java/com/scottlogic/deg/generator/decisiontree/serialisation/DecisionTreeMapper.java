package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.constraints.IConstraint;
import com.scottlogic.deg.generator.constraints.IsEqualToConstantConstraint;
import com.scottlogic.deg.generator.constraints.IsInSetConstraint;
import com.scottlogic.deg.generator.constraints.IsLessThanConstantConstraint;
import com.scottlogic.deg.generator.constraints.IsNullConstraint;
import com.scottlogic.deg.generator.constraints.IsOfTypeConstraint;
import com.scottlogic.deg.generator.constraints.IsStringShorterThanConstraint;
import com.scottlogic.deg.generator.constraints.NotConstraint;
import com.scottlogic.deg.generator.decisiontree.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DecisionTreeMapper {
    // Pair A1
    public DecisionTree fromDto(DecisionTreeDto decisionTreeDto) {
        return new DecisionTree(
            fromDto(decisionTreeDto.rootNode),
            getMappedProfileFields(decisionTreeDto),
            decisionTreeDto.description);
    }
    
    // Pair A2 
    public DecisionTreeDto toDto(DecisionTree tree) {
        DecisionTreeDto dto = new DecisionTreeDto();
        dto.rootNode = toDto(tree.getRootNode());
        dto.fields = tree
                .fields.stream().map(f -> new FieldDto(f.name)).collect(Collectors.toList());
        dto.description = tree.description;
        return dto;
    }
    
    // Used by A1
    private ProfileFields getMappedProfileFields(DecisionTreeDto decisionTreeDto) {
        final List<Field> mappedFields = decisionTreeDto.fields
                .stream()
                .map(f -> new Field(f.name))
                .collect(Collectors.toList());
        
        return new ProfileFields(mappedFields);
    }

    // Pair B1
    private ConstraintNode fromDto(ConstraintNodeDto constraintNodeDto) {
        if (constraintNodeDto.decisions == null || constraintNodeDto.decisions.isEmpty()) {
            // Base case when no more decisions on a constraint node
            return new TreeConstraintNode(getAtomicConstraints(constraintNodeDto), Collections.emptyList());
        }

        List<DecisionNode> nodes = constraintNodeDto.decisions.stream()
                .map(this::fromDto).collect(Collectors.toList());

        return new TreeConstraintNode(getAtomicConstraints(constraintNodeDto), nodes);
    }

    // Pair B2 
    static private ConstraintNodeDto toDto(ConstraintNode node) {
        if (node.getDecisions()==null || node.getDecisions().isEmpty()) {
            // Base case when no more decisions on a constraint node
            ConstraintNodeDto constraintNodeDto = new ConstraintNodeDto();
            constraintNodeDto.atomicConstraints = getAtomicConstraintsDtos(node);
            return constraintNodeDto;
        }
        
        List<DecisionNodeDto> decisionNodeDtos = node
                                        .getDecisions()
                                        .stream()
                                        .map(d -> DecisionTreeMapper.toDto(d))
                                        .collect(Collectors.toList());
        ConstraintNodeDto constraintNodeDto = new ConstraintNodeDto();
        constraintNodeDto.atomicConstraints = getAtomicConstraintsDtos(node);
        constraintNodeDto.decisions = decisionNodeDtos;

        return constraintNodeDto;
    }

    // Pair C1
    private List<IConstraint> getAtomicConstraints(ConstraintNodeDto constraintNodeDto){
        return Optional.ofNullable(constraintNodeDto.atomicConstraints)
                    .map(Collection::stream)
                    .orElseGet(Stream::empty)
                    .map(IConstraintMapper::map)
                    .collect(Collectors.toList());
    }
    
    // Pair C2
    static private List<ConstraintDto> getAtomicConstraintsDtos(ConstraintNode node) {
        return Optional.ofNullable(node.getAtomicConstraints())
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(c->DecisionTreeMapper.toDto(c))
                .collect(Collectors.toList());
    }
    
    // Pair D1
    private DecisionNode fromDto(DecisionNodeDto decisionNodeDto){
        Collection<ConstraintNode> options = decisionNodeDto.options.stream()
                .map(this::fromDto)
                .collect(Collectors.toList());

        return new TreeDecisionNode(options);
    }
    
    // Pair D2  
    static private DecisionNodeDto toDto(DecisionNode node) {
        DecisionNodeDto dto = new DecisionNodeDto();
        dto.options = node
                        .getOptions()
                        .stream()
                        .map(c -> DecisionTreeMapper.toDto(c))
                        .collect(Collectors.toList());
        
        return dto;
    }

    // Parallel to IConstraintMapper.map(), but a different design
    static public ConstraintDto toDto(IConstraint constraint) {
        if (constraint instanceof IsInSetConstraint) {
            return IsInSetConstraintDto.toDto((IsInSetConstraint) constraint);
        } else if (constraint instanceof IsEqualToConstantConstraint) {
            return IsEqualToConstantConstraintDto.toDto((IsEqualToConstantConstraint) constraint);
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return IsStringShorterThanConstraintDto.toDto((IsStringShorterThanConstraint) constraint);
        } else if (constraint instanceof IsOfTypeConstraint) {
            return IsOfTypeConstraintDto.toDto((IsOfTypeConstraint) constraint);
        } else if (constraint instanceof NotConstraint) {
            return NotConstraintDto.toDto((NotConstraint) constraint);
        } else if (constraint instanceof IsNullConstraint) {
            return IsNullConstraintDto.toDto((IsNullConstraint) constraint);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return IsLessThanConstantConstraintDto.toDto((IsLessThanConstantConstraint) constraint);
        } else {
            throw new UnsupportedOperationException("Unsupported Constraint: " 
                        + constraint.getClass().getName());
        }
    }
}

package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.inputs.RuleInformation;
import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.decisiontree.*;
import com.scottlogic.deg.schemas.v0_1.RuleDTO;

import java.util.*;
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
    private List<AtomicConstraint> getAtomicConstraints(ConstraintNodeDto constraintNodeDto){
        return Optional.ofNullable(constraintNodeDto.atomicConstraints)
                    .map(Collection::stream)
                    .orElseGet(Stream::empty)
                    .map(dto-> DecisionTreeMapper.fromDto(dto))
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

    // Pair E1
    static private ConstraintDto toDto(AtomicConstraint constraint) {
        if (constraint instanceof IsInSetConstraint) {
            return toDto((IsInSetConstraint) constraint);
        } else if (constraint instanceof IsStringShorterThanConstraint) {
            return toDto((IsStringShorterThanConstraint) constraint);
        } else if (constraint instanceof IsOfTypeConstraint) {
            return toDto((IsOfTypeConstraint) constraint);
        } else if (constraint instanceof NotConstraint) {
            return toDto((NotConstraint) constraint);
        } else if (constraint instanceof IsNullConstraint) {
            return toDto((IsNullConstraint) constraint);
        } else if (constraint instanceof IsLessThanConstantConstraint) {
            return toDto((IsLessThanConstantConstraint) constraint);
        } else {
            throw new UnsupportedOperationException("Unsupported Constraint: " 
                        + constraint.getClass().getName());
        }
    }

    // Pair E2
    static private AtomicConstraint fromDto(ConstraintDto constraintDto) {
        if (constraintDto instanceof IsInSetConstraintDto) {
            return fromDto((IsInSetConstraintDto) constraintDto);
        } else if (constraintDto instanceof IsStringShorterThanConstraintDto) {
            return fromDto((IsStringShorterThanConstraintDto) constraintDto);
        } else if (constraintDto instanceof IsOfTypeConstraintDto) {
            return fromDto((IsOfTypeConstraintDto) constraintDto);
        } else if (constraintDto instanceof NotConstraintDto) {
            return fromDto((NotConstraintDto) constraintDto);
        } else if (constraintDto instanceof IsNullConstraintDto) {
            return fromDto((IsNullConstraintDto) constraintDto);
        } else if (constraintDto instanceof IsLessThanConstantConstraintDto) {
            return fromDto((IsLessThanConstantConstraintDto) constraintDto);
        } else {
            throw new UnsupportedOperationException("Unsupported Constraint: " 
                        + constraintDto.getClass().getName());
        }
    }
    
    /*
     * Pair F1: 
     */
    static private IsInSetConstraintDto toDto(IsInSetConstraint constraint) {
        IsInSetConstraintDto dto = new IsInSetConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.legalValues = new ArrayList<>(constraint.legalValues);
        return dto;
    }
    
    static private IsLessThanConstantConstraintDto toDto(IsLessThanConstantConstraint constraint) {
        IsLessThanConstantConstraintDto dto = new IsLessThanConstantConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.referenceValue = constraint.referenceValue;
        return dto;
    }
    
    static private IsNullConstraintDto toDto(IsNullConstraint constraint) {
        IsNullConstraintDto dto = new IsNullConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        return dto;
    }
    
    static private IsOfTypeConstraintDto toDto(IsOfTypeConstraint constraint) {
        IsOfTypeConstraintDto dto = new IsOfTypeConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.requiredTypeString = constraint.requiredType.name().toLowerCase();
        return dto;
    }
    
    static private NotConstraintDto toDto(NotConstraint constraint) {
        NotConstraintDto dto = new NotConstraintDto();
        dto.negatedConstraint = DecisionTreeMapper.toDto(constraint.negatedConstraint);
        return dto;
    }
    
    static private IsStringShorterThanConstraintDto toDto(IsStringShorterThanConstraint constraint) {
        IsStringShorterThanConstraintDto dto = new IsStringShorterThanConstraintDto();
        dto.field = new FieldDto(constraint.field.name);
        dto.referenceValue = constraint.referenceValue;
        return dto;
    }
    
    /*
     * Pair F2 
     */
    private static AtomicConstraint fromDto(IsInSetConstraintDto dto) {
        return new IsInSetConstraint(new Field(dto.field.name), new HashSet<>(dto.legalValues), rules(dto.rule));
    }

    private static AtomicConstraint fromDto(IsStringShorterThanConstraintDto dto) {
        return new IsStringShorterThanConstraint(new Field(dto.field.name), dto.referenceValue, rules(dto.rule));
    }
    
    private static AtomicConstraint fromDto(IsOfTypeConstraintDto dto) {
        return new IsOfTypeConstraint(new Field(dto.field.name), dto.getTypesFromTypesDto(), rules(dto.rule));
    }
    
    private static AtomicConstraint fromDto(NotConstraintDto dto) {
        return DecisionTreeMapper.fromDto(dto.negatedConstraint).negate();
    }
    
    private static AtomicConstraint fromDto(IsNullConstraintDto dto) {
        return new IsNullConstraint(new Field(dto.field.name), rules(dto.rule));
    }

    private static AtomicConstraint fromDto(IsLessThanConstantConstraintDto dto) {
        return new IsLessThanConstantConstraint(new Field(dto.field.name), dto.referenceValue, rules(dto.rule));
    }

    private static Set<RuleInformation> rules(String name){
        return Collections.singleton(new RuleInformation(name));
    }
}

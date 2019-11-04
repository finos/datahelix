package com.scottlogic.deg.profile.creation.services;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.InMapConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.FieldDTO;
import com.scottlogic.deg.profile.creation.dtos.RuleDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.relations.InMapConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.creation.dtos.constraints.grammatical.ConditionalConstraintDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldService
{
    public Fields createFields(List<FieldDTO> fieldDTOs, List<RuleDTO> ruleDTOs)
    {
        List<Field> fields = fieldDTOs.stream().map(this::createRegularField).collect(Collectors.toList());
        getInMapFieldNames(ruleDTOs).stream().map(this::createInMapField).forEach(fields::add);
        return new Fields(fields);
    }

    private Field createRegularField(FieldDTO fieldDTO)
    {
        String formatting = fieldDTO.formatting != null ? fieldDTO.formatting : fieldDTO.type.getDefaultFormatting();
        return new Field(fieldDTO.name, fieldDTO.type, fieldDTO.unique,formatting, false, fieldDTO.nullable);
    }

    private Field createInMapField(String inMapFile)
    {
        return new Field(inMapFile, SpecificFieldType.INTEGER, false, null, true, false);
    }

    private List<String> getInMapFieldNames(List<RuleDTO> ruleDTOs)
    {
        return ruleDTOs.stream()
            .flatMap(ruleDTO -> ruleDTO.constraints.stream())
            .flatMap(constraint -> getAllAtomicConstraints(Stream.of(constraint)))
            .filter(constraintDTO -> constraintDTO.getType() == ConstraintType.IN_MAP)
            .map(constraintDTO -> ((InMapConstraintDTO) constraintDTO).otherField)
            .distinct()
            .collect(Collectors.toList());
    }

    private Stream<ConstraintDTO> getAllAtomicConstraints(Stream<ConstraintDTO> constraintDTOs)
    {
        return constraintDTOs.flatMap(this::getAllAtomicSubConstraints);
    }

    private Stream<ConstraintDTO> getAllAtomicSubConstraints(ConstraintDTO constraintDTO)
    {
        switch (constraintDTO.getType())
        {
            case IF:
                ConditionalConstraintDTO conditionalConstraintDTO = (ConditionalConstraintDTO) constraintDTO;
                return getAllAtomicConstraints(conditionalConstraintDTO.elseConstraint == null
                    ? Stream.of(((ConditionalConstraintDTO) constraintDTO).thenConstraint)
                    : Stream.of(((ConditionalConstraintDTO) constraintDTO).thenConstraint, ((ConditionalConstraintDTO) constraintDTO).elseConstraint));
            case ALL_OF:
                return getAllAtomicConstraints(((AllOfConstraintDTO) constraintDTO).constraints.stream());
            case ANY_OF:
                return getAllAtomicConstraints(((AnyOfConstraintDTO) constraintDTO).constraints.stream());
            default:
                return Stream.of(constraintDTO);
        }
    }
}

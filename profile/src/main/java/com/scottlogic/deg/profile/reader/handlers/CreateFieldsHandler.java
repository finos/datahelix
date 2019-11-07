package com.scottlogic.deg.profile.reader.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.profile.SpecificFieldType;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.reader.commands.CreateFields;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.dtos.constraints.*;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AllOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.AnyOfConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateFieldsHandler extends CommandHandler<CreateFields, Fields>
{
    public CreateFieldsHandler(Validator<CreateFields> validator)
    {
        super(validator);
    }

    @Override
    public CommandResult<Fields> handleCommand(CreateFields command)
    {
        List<Field> fields = createFields(command.fieldDTOs);
        getInMapFiles(command.ruleDTOs).stream().map(this::createField).forEach(fields::add);
        return CommandResult.success(new Fields(fields));
    }

    private List<Field> createFields(List<FieldDTO> fieldDTOs)
    {
        return fieldDTOs.stream().map(this::createField).collect(Collectors.toList());
    }

    private Field createField(FieldDTO fieldDTO)
    {
        String formatting = fieldDTO.formatting != null ? fieldDTO.formatting : fieldDTO.type.getDefaultFormatting();
        return new Field(fieldDTO.name, fieldDTO.type, fieldDTO.unique,formatting, false, fieldDTO.nullable);
    }

    private Field createField(String inMapFile)
    {
        return new Field(inMapFile, SpecificFieldType.INTEGER, false, null, true, false);
    }

    private List<String> getInMapFiles(List<RuleDTO> ruleDTOs)
    {
        return ruleDTOs.stream()
            .flatMap(ruleDTO -> ruleDTO.constraints.stream())
            .flatMap(constraint -> getAllAtomicConstraints(Stream.of(constraint)))
            .filter(constraintDTO -> constraintDTO.getType() == ConstraintType.IN_MAP)
            .map(constraintDTO -> ((InMapConstraintDTO) constraintDTO).file)
            .distinct()
            .collect(Collectors.toList());
    }

    private Stream<ConstraintDTO> getAllAtomicConstraints(Stream<ConstraintDTO> constraintDTOs)
    {
        return constraintDTOs.flatMap(this::getUnpackedConstraintsToStream);
    }

    private Stream<ConstraintDTO> getUnpackedConstraintsToStream(ConstraintDTO constraintDTO)
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

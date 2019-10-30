package com.scottlogic.deg.profile.handlers;

import an.awesome.pipelinr.Pipeline;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.commands.CreateField;
import com.scottlogic.deg.profile.commands.CreateFields;
import com.scottlogic.deg.profile.common.ConstraintType;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.dtos.constraints.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CreateFieldsHandler extends CommandHandler<CreateFields, ProfileFields>
{
    private final Pipeline pipeline;

    public CreateFieldsHandler(Pipeline pipeline, Validator<CreateFields> validator)
    {
        super(validator);
        this.pipeline = pipeline;
    }

    @Override
    protected CommandResult<ProfileFields> handleCommand(CreateFields command)
    {
        CommandResult<List<Field>> createFieldsResult = createFields(command.fieldDTOs);
        if(!createFieldsResult.hasValue)
        {
            return CommandResult.failure(createFieldsResult.errors);
        }
        List<Field> fields = createFieldsResult.value;
        inMapFiles(command.ruleDTOs).stream()
            .map(this::createInMapField)
            .forEach(fields::add);

        return CommandResult.success(new ProfileFields(fields));
    }

    private CommandResult<List<Field>> createFields(List<FieldDTO> fieldDTOs)
    {
        return CommandResult.combine(fieldDTOs.stream()
        .map(dto -> pipeline.send(new CreateField(dto)))
        .collect(Collectors.toList()));
    }

    private Field createInMapField(String file)
    {
        return new Field(file, FieldType.NUMERIC, false, null, true);
    }


    private List<String> inMapFiles(List<RuleDTO> rules)
    {
        return rules.stream()
            .flatMap(ruleDTO -> ruleDTO.constraints.stream())
            .flatMap(constraint -> getAllAtomicConstraints(Stream.of(constraint)))
            .filter(constraintDTO -> constraintDTO.getType() == ConstraintType.IN_MAP)
            .map(constraintDTO -> ((InMapConstraintDTO) constraintDTO).file)
            .collect(Collectors.toList());
    }


    private Stream<ConstraintDTO> getAllAtomicConstraints(Stream<ConstraintDTO> constraints)
    {
        return constraints.flatMap(this::getUnpackedConstraintsToStream);
    }

    private Stream<ConstraintDTO> getUnpackedConstraintsToStream(ConstraintDTO constraintDTO) {
        switch (constraintDTO.getType())
        {
            case IF:
                IfConstraintDTO conditionalConstraintDTO = (IfConstraintDTO) constraintDTO;
                return getAllAtomicConstraints(conditionalConstraintDTO.elseConstraint == null
                    ? Stream.of(((IfConstraintDTO) constraintDTO).thenConstraint)
                    : Stream.of(((IfConstraintDTO) constraintDTO).thenConstraint, ((IfConstraintDTO) constraintDTO).elseConstraint));
            case ALL_OF:
                return getAllAtomicConstraints(((AllOfConstraintDTO) constraintDTO).constraints.stream());
            case ANY_OF:
                return getAllAtomicConstraints(((AnyOfConstraintDTO) constraintDTO).constraints.stream());
            default:
                return Stream.of(constraintDTO);
        }
    }
}

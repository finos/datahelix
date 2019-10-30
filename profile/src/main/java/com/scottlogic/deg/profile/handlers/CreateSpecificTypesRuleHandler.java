package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.RuleInformation;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.commands.CreateSpecificTypesRule;
import com.scottlogic.deg.profile.dtos.constraints.NullConstraintDTO;
import com.scottlogic.deg.profile.reader.atomic.FieldReader;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreateSpecificTypesRuleHandler extends CommandHandler<CreateSpecificTypesRule, Optional<Rule>>
{
    public CreateSpecificTypesRuleHandler(Validator<CreateSpecificTypesRule> validator)
    {
        super(validator);
    }

    @Override
    protected CommandResult<Optional<Rule>> handleCommand(CreateSpecificTypesRule command)
    {

        List<Constraint> constraints = command.fields.stream()
            .map(field -> FieldReader.read(field, field.type))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
        return constraints.isEmpty()
            ? CommandResult.success(Optional.empty())
            : CommandResult.success(Optional.of(new Rule(new RuleInformation("non-nullable"), constraints)));
    }

    private Constraint createNullConstraint(Field field, Fields fields)
    {
        NullConstraintDTO nullConstraintDTO = new NullConstraintDTO();
        nullConstraintDTO.field = field.name;
        return constraintReader.read(nullConstraintDTO, fields);
    }
}

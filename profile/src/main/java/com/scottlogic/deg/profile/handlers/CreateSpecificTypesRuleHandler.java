package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.commands.CreateSpecificTypesRule;
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
        List<Constraint> specificTypeConstraints = command.fields.stream()
            .map(FieldReader::read)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        return specificTypeConstraints.isEmpty()
            ? CommandResult.success(Optional.empty())
            : CommandResult.success(Optional.of(new Rule("specific-type", specificTypeConstraints)));
    }
}

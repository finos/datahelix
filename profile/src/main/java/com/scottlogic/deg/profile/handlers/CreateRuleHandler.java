package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.commands.CreateRule;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.reader.ConstraintReader;

import java.util.List;
import java.util.stream.Collectors;

public class CreateRuleHandler extends CommandHandler<CreateRule, Rule>
{
    private final ConstraintReader constraintReader;

    public CreateRuleHandler(ConstraintReader constraintReader, Validator<CreateRule> validator)
    {
        super(validator);
        this.constraintReader = constraintReader;
    }

    @Override
    protected CommandResult<Rule> handleCommand(CreateRule command)
    {
        CommandResult<List<Constraint>> createConstraintsResult = CommandResult.combine(command.dto.constraints.stream()
            .map(dto -> createConstraint(dto, command.fields))
            .collect(Collectors.toList()));

        return createConstraintsResult.hasValue
            ? CommandResult.success(new Rule(command.dto.description, createConstraintsResult.value))
            : CommandResult.failure(createConstraintsResult.errors);
    }

    private CommandResult<Constraint> createConstraint(ConstraintDTO dto, Fields fields)
    {
        return CommandResult.success(constraintReader.read(dto, fields));
    }
}

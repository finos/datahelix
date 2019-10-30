package com.scottlogic.deg.profile.handlers;

import an.awesome.pipelinr.Pipeline;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.profile.commands.CreateConstraint;
import com.scottlogic.deg.profile.commands.CreateRule;

import java.util.List;
import java.util.stream.Collectors;

public class CreateRuleHandler extends CommandHandler<CreateRule, Rule>
{
    private final Pipeline pipeline;

    public CreateRuleHandler(Pipeline pipeline, Validator<CreateRule> validator)
    {
        super(validator);
        this.pipeline = pipeline;
    }

    @Override
    protected CommandResult<Rule> handleCommand(CreateRule command)
    {
        List<CommandResult<Constraint>> createConstraintResults = command.dto.constraints.stream()
            .map(dto -> pipeline.send(new CreateConstraint(dto)))
            .collect(Collectors.toList());

        CommandResult<List<Constraint>> createConstraintsCombinedResult = CommandResult.combine(createConstraintResults);

        return createConstraintsCombinedResult.hasValue
            ? CommandResult.success(new Rule(command.dto.description, createConstraintsCombinedResult.value))
            : CommandResult.failure(createConstraintsCombinedResult.errors);
    }
}

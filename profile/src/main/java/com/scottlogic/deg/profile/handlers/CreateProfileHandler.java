package com.scottlogic.deg.profile.handlers;

import an.awesome.pipelinr.Pipeline;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.profile.commands.*;
import com.scottlogic.deg.profile.dtos.RuleDTO;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreateProfileHandler extends CommandHandler<CreateProfile, Profile>
{
    private final Pipeline pipeline;

    public CreateProfileHandler(Pipeline pipeline, Validator<CreateProfile> validator)
    {
        super(validator);
        this.pipeline = pipeline;
    }

    @Override
    protected CommandResult<Profile> handleCommand(CreateProfile command)
    {
        CreateFields createFields = new CreateFields(command.dto.fields, command.dto.rules);
        CommandResult<ProfileFields> createFieldsResult = pipeline.send(createFields);
        CommandResult<List<Rule>> createRulesResult = createRules(command.dto.rules);

        if(createFieldsResult.hasValue || !createRulesResult.hasValue)
        {
            return CommandResult.failure(createFieldsResult.errors, createRulesResult.errors);
        }

        ProfileFields fields = createFieldsResult.value;
        List<Rule> rules = createRulesResult.value;

        CommandResult<Optional<Rule>> createNonNullableRuleResult = pipeline.send(new CreateNonNullableRule(fields));
        CommandResult<Optional<Rule>> createSpecificTypesRuleResult  = pipeline.send(new CreateSpecificTypesRule(fields));

        if(!createNonNullableRuleResult.hasValue || !createSpecificTypesRuleResult.hasValue)
        {
            return CommandResult.failure(createNonNullableRuleResult.errors, createSpecificTypesRuleResult.errors);
        }

        createNonNullableRuleResult.value.ifPresent(rules::add);
        createSpecificTypesRuleResult.value.ifPresent(rules::add);

        return CommandResult.success(new Profile(fields, rules, command.dto.description));
    }

    private CommandResult<List<Rule>> createRules(Collection<RuleDTO> ruleDTOs)
    {
        List<CommandResult<Rule>> createRuleResults = ruleDTOs.stream()
            .map(dto -> pipeline.send(new CreateRule(dto)))
            .collect(Collectors.toList());

        return CommandResult.combine(createRuleResults);
    }
}

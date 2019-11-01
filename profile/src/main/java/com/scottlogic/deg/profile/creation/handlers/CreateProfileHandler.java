package com.scottlogic.deg.profile.creation.handlers;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.profile.creation.commands.CreateProfile;
import com.scottlogic.deg.profile.creation.services.FieldService;
import com.scottlogic.deg.profile.creation.services.RuleService;

import java.util.List;

public class CreateProfileHandler extends CommandHandler<CreateProfile, Profile>
{
    private final FieldService fieldService;
    private final RuleService ruleService;

    @Inject
    public CreateProfileHandler(FieldService fieldService, RuleService ruleService, Validator<CreateProfile> validator)
    {
        super(validator);
        this.fieldService = fieldService;
        this.ruleService = ruleService;
    }

    @Override
    public CommandResult<Profile> handleCommand(CreateProfile command)
    {
        Fields fields = fieldService.createFields(command.profileDTO.fields, command.profileDTO.rules);
        List<Rule> rules = ruleService.createRules(command.profileDTO.rules, fields);
        return CommandResult.success(new Profile(fields, rules, command.profileDTO.description));
    }
}

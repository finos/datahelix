/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.profile.handlers;

import com.google.inject.Inject;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.profile.commands.CreateProfile;
import com.scottlogic.deg.profile.services.FieldService;
import com.scottlogic.deg.profile.services.RuleService;

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

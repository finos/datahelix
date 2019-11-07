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
package com.scottlogic.deg.profile.reader.handlers;

import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.DateTimeGranularity;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.profile.NumericGranularity;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.*;
import com.scottlogic.deg.profile.reader.commands.CreateConstraints;
import com.scottlogic.deg.profile.reader.commands.CreateFields;
import com.scottlogic.deg.profile.reader.commands.CreateProfile;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.reader.NameRetriever;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CreateProfileHandler extends CommandHandler<CreateProfile, Profile>
{
    private final CommandBus bus;

    public CreateProfileHandler(CommandBus bus, Validator<CreateProfile> validator)
    {
        super(validator);
        this.bus = bus;
    }

    @Override
    public CommandResult<Profile> handleCommand(CreateProfile command)
    {
        CommandResult<Fields> createFieldsResult = bus.send(new CreateFields(command.profileDTO.fields, command.profileDTO.rules));
        if (!createFieldsResult.isSuccess) return CommandResult.failure(createFieldsResult.errors);
        Fields fields = createFieldsResult.value;

        CommandResult<List<Rule>> createRulesResult = createRules(command.profileDTO.rules, fields);
        if (!createRulesResult.isSuccess) return CommandResult.failure(createRulesResult.errors);
        List<Rule> rules = createRulesResult.value;

        createNotNullableRule(fields).ifPresent(rules::add);
        createSpecificTypeRule(fields).ifPresent(rules::add);

        return CommandResult.success(new Profile(fields, rules, command.profileDTO.description));
    }

    private CommandResult<List<Rule>> createRules(List<RuleDTO> ruleDTOs, Fields fields)
    {
        return CommandResult.combine(ruleDTOs.stream()
            .map(ruleDTO -> createRule(ruleDTO, fields))
            .collect(Collectors.toList()));
    }

    private CommandResult<Rule> createRule(RuleDTO ruleDTO, Fields fields)
    {
        return bus.send(new CreateConstraints(ruleDTO.constraints, fields))
            .map(constraints -> new Rule(ruleDTO.description, constraints));
    }

    private Optional<Rule> createNotNullableRule(Fields fields)
    {
        List<Constraint> notNullableConstraints =  fields.stream()
            .filter(field -> !field.isNullable())
            .map(field -> new IsNullConstraint(field).negate())
            .collect(Collectors.toList());

        return notNullableConstraints.isEmpty()
            ? Optional.empty()
            : Optional.of(new Rule("not-nullable", notNullableConstraints));
    }

    private Optional<Rule> createSpecificTypeRule(Fields fields)
    {
        List<Constraint> specificTypeConstraints = fields.stream()
            .map(this::createSpecificTypeConstraint)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

         return specificTypeConstraints.isEmpty()
             ? Optional.empty()
             : Optional.of(new Rule("specific-types", specificTypeConstraints));
    }
    
    private Optional<Constraint> createSpecificTypeConstraint(Field field)
    {
        switch (field.getSpecificType()) {
            case DATE:
                return Optional.of(new GranularToDateConstraint(field, new DateTimeGranularity(ChronoUnit.DAYS)));
            case INTEGER:
                return Optional.of(new GranularToNumericConstraint(field, NumericGranularity.create(BigDecimal.ONE)));
            case ISIN:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.ISIN));
            case SEDOL:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.SEDOL));
            case CUSIP:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.CUSIP));
            case RIC:
                return Optional.of(new MatchesStandardConstraint(field, StandardConstraintTypes.RIC));
            case FIRST_NAME:
                return Optional.of(new InSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.FIRST)));
            case LAST_NAME:
                return Optional.of(new InSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.LAST)));
            case FULL_NAME:
                return Optional.of(new InSetConstraint(field, NameRetriever.loadNamesFromFile(NameConstraintTypes.FULL)));
            default:
                return Optional.empty();
        }
    }
}

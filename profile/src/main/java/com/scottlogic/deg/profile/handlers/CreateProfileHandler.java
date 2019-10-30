package com.scottlogic.deg.profile.handlers;

import com.scottlogic.deg.common.commands.CommandBus;
import com.scottlogic.deg.common.commands.CommandHandler;
import com.scottlogic.deg.common.commands.CommandResult;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.generator.profile.Profile;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.profile.commands.CreateFields;
import com.scottlogic.deg.profile.commands.CreateProfile;
import com.scottlogic.deg.profile.commands.CreateRule;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.reader.FieldReader;

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
    protected CommandResult<Profile> handleCommand(CreateProfile command)
    {
        CommandResult<Fields> createFieldsResult = bus.send(new CreateFields(command.dto.fields, command.dto.rules));
        if (!createFieldsResult.hasValue) return CommandResult.failure(createFieldsResult.errors);
        Fields fields = createFieldsResult.value;

        CommandResult<List<Rule>> createRulesResult = createRules(command.dto.rules, fields);
        if (!createRulesResult.hasValue) return CommandResult.failure(createRulesResult.errors);
        List<Rule> rules = createRulesResult.value;

        createNotNullableRule(fields).ifPresent(rules::add);
        createSpecificTypeRule(fields).ifPresent(rules::add);

        return CommandResult.success(new Profile(fields, rules, command.dto.description));
    }

    private CommandResult<List<Rule>> createRules(List<RuleDTO> ruleDTOs, Fields fields)
    {
        return CommandResult.combine(ruleDTOs.stream()
            .map(dto -> bus.send(new CreateRule(fields, dto)))
            .collect(Collectors.toList()));
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
            .map(FieldReader::read)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

         return specificTypeConstraints.isEmpty()
             ? Optional.empty()
             : Optional.of(new Rule("specific-types", specificTypeConstraints));
    }
}

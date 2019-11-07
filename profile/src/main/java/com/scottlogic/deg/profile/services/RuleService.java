package com.scottlogic.deg.profile.services;

import com.google.inject.Inject;
import com.scottlogic.deg.common.profile.Fields;
import com.scottlogic.deg.generator.profile.Rule;
import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.profile.dtos.RuleDTO;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RuleService
{
    private final ConstraintService constraintService;

    @Inject
    public RuleService(ConstraintService constraintService){

        this.constraintService = constraintService;
    }

    public List<Rule> createRules(List<RuleDTO> ruleDTOs, Fields fields)
    {
        List<Rule> rules =  ruleDTOs.stream()
            .map(dto -> new Rule(dto.description, constraintService.createConstraints(dto.constraints, fields)))
            .collect(Collectors.toList());

        createNotNullableRule(fields).ifPresent(rules::add);
        createSpecificTypeRule(fields).ifPresent(rules::add);

        return rules;
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
            .map(constraintService::createSpecificTypeConstraint)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());

        return specificTypeConstraints.isEmpty()
            ? Optional.empty()
            : Optional.of(new Rule("specific-types", specificTypeConstraints));
    }
}

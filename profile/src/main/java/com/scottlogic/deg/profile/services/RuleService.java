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

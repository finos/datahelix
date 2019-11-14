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

package com.scottlogic.deg.profile.validators.profile;

import com.google.inject.Inject;
import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.common.validators.Validator;
import com.scottlogic.deg.generator.generation.GenerationConfigSource;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.ProfileDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;
import com.scottlogic.deg.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.atomic.AtomicConstraintDTO;
import com.scottlogic.deg.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;
import com.scottlogic.deg.profile.services.FieldService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.scottlogic.deg.generator.config.detail.CombinationStrategyType.MINIMAL;

public class ProfileValidator implements Validator<ProfileDTO>
{
    private final GenerationConfigSource configSource;

    @Inject
    public ProfileValidator(GenerationConfigSource configSource)
    {
        this.configSource = configSource;
    }

    @Override
    public ValidationResult validate(ProfileDTO dto)
    {
        List<FieldDTO> fields = dto.fields;
        List<RuleDTO> rules = dto.rules;

        ValidationResult fieldsMustBeValid = fieldsMustBeValid(fields);
        if (!fieldsMustBeValid.isSuccess) return fieldsMustBeValid;

        ValidationResult rulesMustBeValid = rulesMustBeValid(rules, fields);
        if (!rulesMustBeValid.isSuccess) return rulesMustBeValid;

        return ValidationResult.combine(
            uniqueFieldsMustNotBeInIfStatements(dto),
            uniqueFieldsMustNotBePresentUsingMinimalCombinationStrategy(dto));
    }


    private ValidationResult fieldsMustBeSpecified(List<FieldDTO> fields)
    {
        return fields != null && !fields.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Fields must be specified");
    }

    private ValidationResult fieldsMustBeUnique(List<FieldDTO> fields)
    {
        final Set<String> fieldNames = new HashSet<>();
        final Set<String> duplicateFieldNames = new HashSet<>();

        fields.forEach(field ->
        {
            if(!fieldNames.add(field.name))
            {
                duplicateFieldNames.add(field.name);
            }
        });

        return duplicateFieldNames.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Field names must be unique | Duplicates: "
            + String.join(", ", duplicateFieldNames));
    }

    private ValidationResult fieldsMustBeValid(List<FieldDTO> fields)
    {
        ValidationResult fieldsMustBeSpecified = fieldsMustBeSpecified(fields);
        if (!fieldsMustBeSpecified.isSuccess) return fieldsMustBeSpecified;

        ValidationResult fieldsMustBeUnique  = fieldsMustBeUnique(fields);
        if(!fieldsMustBeUnique.isSuccess) return fieldsMustBeUnique;

        FieldValidator fieldValidator = new FieldValidator();
        return ValidationResult.combine(fields.stream().map(fieldValidator::validate));
    }

    private ValidationResult rulesMustBeSpecified(List<RuleDTO> rules)
    {
        return rules != null
            ? ValidationResult.success()
            : ValidationResult.failure("Rules must be specified");
    }

    private ValidationResult rulesMustBeValid(List<RuleDTO> rules, List<FieldDTO> fields)
    {
        ValidationResult rulesMustBeSpecified = rulesMustBeSpecified(rules);
        if (!rulesMustBeSpecified.isSuccess) return rulesMustBeSpecified;

        RuleValidator ruleValidator = new RuleValidator(fields);
        return ValidationResult.combine(rules.stream().map(ruleValidator::validate));
    }

    private ValidationResult uniqueFieldsMustNotBeInIfStatements(ProfileDTO dto)
    {
        List<String> uniqueFields = dto.fields.stream()
            .filter(fieldDTO -> fieldDTO.unique)
            .map(fieldDTO -> fieldDTO.name)
            .collect(Collectors.toList());

        Stream<ConstraintDTO> ifConstraints = dto.rules.stream()
            .flatMap(ruleDTO -> ruleDTO.constraints.stream())
            .filter(constraintDTO -> constraintDTO instanceof ConditionalConstraintDTO)
            .map(constraintDTO -> ((ConditionalConstraintDTO) constraintDTO).ifConstraint);

        Set<String> ifConstraintFields = FieldService.getAllAtomicConstraints(ifConstraints)
            .map(constraintDTO -> ((AtomicConstraintDTO) constraintDTO).field)
            .collect(Collectors.toSet());

        List<String> errors = uniqueFields.stream().filter(ifConstraintFields::contains)
            .map(f -> "Unique field "+f+" cannot be referenced in IF statement")
            .collect(Collectors.toList());

        return errors.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure(errors);
    }

    private ValidationResult uniqueFieldsMustNotBePresentUsingMinimalCombinationStrategy(ProfileDTO dto)
    {
        if(configSource == null)
        {
            return ValidationResult.success();
        }
        if (configSource.getCombinationStrategyType() != MINIMAL && dto.fields.stream().anyMatch(f -> f.unique))
        {
            return ValidationResult.failure("Unique fields do not work when not using Minimal combination strategy");
        }
        return ValidationResult.success();
    }
}

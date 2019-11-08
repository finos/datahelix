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

import com.scottlogic.deg.common.validators.ValidationResult;
import com.scottlogic.deg.common.validators.Validator;
import com.scottlogic.deg.profile.dtos.FieldDTO;
import com.scottlogic.deg.profile.dtos.RuleDTO;

import java.util.List;

public class RuleValidator implements Validator<RuleDTO>
{
    private final List<FieldDTO> fields;

    public RuleValidator(List<FieldDTO> fields)
    {

        this.fields = fields;
    }

    @Override
    public ValidationResult validate(RuleDTO rule)
    {
        String ruleDescription = rule.description == null ? "Unnamed rule" : rule.description;
        ValidationResult constraintsMustBeSpecified = constraintsMustBeSpecified(rule, ruleDescription);
        if(!constraintsMustBeSpecified.isSuccess) return constraintsMustBeSpecified;
        return ValidationResult.combine(rule.constraints.stream().map(c -> ConstraintValidator.validateConstraint(c, ruleDescription, fields)));
    }

    private ValidationResult constraintsMustBeSpecified(RuleDTO rule, String ruleDescription)
    {
        return rule.constraints != null && !rule.constraints.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Constraints must be specified | Rule: " + ruleDescription);
    }

}

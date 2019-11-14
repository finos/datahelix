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

package com.scottlogic.datahelix.generator.profile.validators.profile.constraints.grammatical;

import com.scottlogic.datahelix.generator.common.validators.ValidationResult;
import com.scottlogic.datahelix.generator.profile.dtos.FieldDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.ConstraintDTO;
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.GrammaticalConstraintDTO;
import com.scottlogic.datahelix.generator.profile.validators.profile.ConstraintValidator;

import java.util.List;

abstract class GrammaticalConstraintValidator<T extends GrammaticalConstraintDTO> extends ConstraintValidator<T>
{
    GrammaticalConstraintValidator(String rule, List<FieldDTO> fields)
    {
        super(rule, fields);
    }

    ValidationResult subConstraintsMustBeValid(List<ConstraintDTO> subConstraints, T grammaticalConstraint)
    {
        ValidationResult subConstraintsMustBeSpecified = subConstraintsMustBeSpecified(subConstraints, grammaticalConstraint);
        if(!subConstraintsMustBeSpecified.isSuccess) return subConstraintsMustBeSpecified;

        return ValidationResult.combine(subConstraints.stream().map(c -> validateConstraint(c, rule, fields)));
    }

    private ValidationResult subConstraintsMustBeSpecified(List<ConstraintDTO> constraints, T constraint)
    {
        return constraints != null && !constraints.isEmpty()
            ? ValidationResult.success()
            : ValidationResult.failure("Sub constraints must be specified" + getErrorInfo(constraint));
    }
}

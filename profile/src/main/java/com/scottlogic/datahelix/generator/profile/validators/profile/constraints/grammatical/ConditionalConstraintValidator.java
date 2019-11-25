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
import com.scottlogic.datahelix.generator.profile.dtos.constraints.grammatical.ConditionalConstraintDTO;

import java.util.List;

public class ConditionalConstraintValidator  extends GrammaticalConstraintValidator<ConditionalConstraintDTO>
{
    public ConditionalConstraintValidator(List<FieldDTO> fields)
    {
        super(fields);
    }

    @Override
    public ValidationResult validate(ConditionalConstraintDTO conditionalConstraint)
    {
        ValidationResult validateIfConstraint = validateConstraint(conditionalConstraint.ifConstraint, fields);
        ValidationResult validateThenConstraint = validateConstraint(conditionalConstraint.thenConstraint, fields);
        ValidationResult validateElseConstraint = conditionalConstraint.elseConstraint == null
            ? ValidationResult.success()
            :validateConstraint(conditionalConstraint.ifConstraint, fields);

        return ValidationResult.combine(validateIfConstraint, validateThenConstraint, validateElseConstraint);
    }
}

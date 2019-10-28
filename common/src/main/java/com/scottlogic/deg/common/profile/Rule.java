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

package com.scottlogic.deg.common.profile;

import com.scottlogic.deg.common.ValidationException;
import com.scottlogic.deg.common.profile.constraints.Constraint;

import java.util.Collection;

public class Rule
{
    private final String description;
    private final Collection<Constraint> constraints;

    private Rule(String description, Collection<Constraint> constraints)
    {
        this.description = description;
        this.constraints = constraints;
    }

    public static Rule create(String description, Collection<Constraint> constraints)
    {
        constraintCollectionMustNotBeNullOrEmpty(constraints);
        return new Rule(description != null ? description : "Unnamed rule", constraints);
    }

    //region<Validation>

    private static void constraintCollectionMustNotBeNullOrEmpty(Collection<Constraint> constraints)
    {
        if (constraints == null)
        {
            throw new ValidationException("Rule - constraint collection must not be null");
        }
        if (constraints .isEmpty())
        {
            throw new ValidationException("Rule - constraint collection must not be empty");
        }
    }

    //endregion

    public String getDescription() {
        return description;
    }

    public Collection<Constraint> getConstraints() {
        return constraints;
    }

}



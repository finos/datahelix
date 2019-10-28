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
import com.scottlogic.deg.common.profile.fields.Fields;

import java.util.Collection;

public class Profile
{
    private final String description;
    private final Fields fields;
    private final Collection<Rule> rules;

    private Profile(String description, Fields fields, Collection<Rule> rules)
    {
        this.description = description;
        this.fields = fields;
        this.rules = rules;
    }

    public static Profile create(String description, Fields fields, Collection<Rule> rules)
    {
        fieldsMustNotBeNull(fields);
        ruleCollectionMustNotBeNull(rules);

        //rules.stream().flatMap(rule -> rule.getConstraints().stream())
        // .filter(constraint -> constraint instanceof AtomicConstraint)
        // .forEach(constraint -> atomicConstraintFieldMustBeInFields((AtomicConstraint)constraint, fields));

        return new Profile(description, fields, rules);
    }

    public static Profile create(Fields fields, Collection<Rule> rules)
    {
        return Profile.create(null, fields, rules);
    }

    //region<Validation>

    private static void fieldsMustNotBeNull(Fields fields)
    {
        if(fields == null)
        {
            throw new ValidationException("Profile - fields must not be null");
        }
    }

    private static void ruleCollectionMustNotBeNull(Collection<Rule> rules)
    {
        if (rules == null)
        {
            throw new ValidationException("Profile - rule collection must not be null");
        }
    }

   /* TODO
    private static void atomicConstraintFieldMustBeInFields(AtomicConstraint constraint, Fields fields)
    {
        String constraintFieldName = constraint.getField().getName();
        if(fields.getByName(constraintFieldName) == null)
        {
            throw new ValidationException("Profile - atomic constraint references non-existent field: " + constraintFieldName);
        }
    }
    */

    //endregion

    //region<Getters>

    public String getDescription()
    {
        return description;
    }

    public Fields getFields()
    {
        return fields;
    }

    public Collection<Rule> getRules()
    {
        return rules;
    }

    //endregion
}

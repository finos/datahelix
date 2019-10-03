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

package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.profile.constraints.Constraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsInSetConstraint;
import com.scottlogic.deg.generator.profile.constraints.atomic.IsNullConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.AndConstraint;
import com.scottlogic.deg.generator.profile.constraints.grammatical.ConditionalConstraint;
import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;

import java.util.*;
import java.util.stream.Collectors;

public class ConstraintBuilder {
    private final List<Constraint> constraints = new ArrayList<>();
    private final Map<String, Field> fields;

    public ConstraintBuilder(List<Field> fields) {
        this.fields = fields.stream().collect(Collectors.toMap(f -> f.name, f -> f));
    }

    public List<Constraint> build() {
        return constraints;
    }

    public ConstraintBuilder addInSetConstraint(String fieldname, List<Object> values) {
        constraints.add(new IsInSetConstraint(fields.get(fieldname),
            DistributedList.uniform(values)));
        return this;
    }

    public ConstraintBuilder addEqualToConstraint(String fieldname, Object value) {
        constraints.add(new IsInSetConstraint(
            fields.get(fieldname),
            DistributedList.singleton(value)));
        return this;
    }

    public ConstraintBuilder addConditionalConstraint(List<Constraint> predicates, List<Constraint> consequences) {
        constraints.add(new ConditionalConstraint(new AndConstraint(predicates), new AndConstraint(consequences)));
        return this;
    }

    public ConstraintBuilder addNullConstraint(String fieldName) {
        constraints.add(new IsNullConstraint(fields.get(fieldName)));
        return this;
    }
}

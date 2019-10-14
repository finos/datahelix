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

package com.scottlogic.deg.generator.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;
import com.scottlogic.deg.generator.fieldspecs.FieldSpec;
import com.scottlogic.deg.generator.fieldspecs.FieldSpecFactory;
import com.scottlogic.deg.generator.restrictions.MatchesStandardStringRestrictions;
import com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory;

import java.util.Objects;
import java.util.regex.Pattern;

import static com.scottlogic.deg.generator.profile.constraints.atomic.StandardConstraintTypes.RIC;

public class MatchesStandardConstraint implements AtomicConstraint {
    public final Field field;
    public final StandardConstraintTypes standard;

    public MatchesStandardConstraint(Field field, StandardConstraintTypes standard) {
        this.field = field;
        this.standard = standard;
    }

    @Override
    public String toString(){
        return String.format("%s is a %s", field.name, standard.getClass().getName());
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public AtomicConstraint negate() {
        return new NotMatchesStandardConstraint(field, standard);
    }

    @Override
    public FieldSpec toFieldSpec() {
        if (standard.equals(RIC)) {
            return FieldSpecFactory.fromRestriction(StringRestrictionsFactory.forStringMatching(Pattern.compile(RIC.getRegex()), false));
        }

        return FieldSpecFactory.fromRestriction(new MatchesStandardStringRestrictions(standard));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        MatchesStandardConstraint constraint = (MatchesStandardConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(standard, constraint.standard);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, standard);
    }
}


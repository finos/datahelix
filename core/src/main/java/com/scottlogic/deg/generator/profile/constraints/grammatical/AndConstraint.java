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

package com.scottlogic.deg.generator.profile.constraints.grammatical;

import com.scottlogic.deg.generator.profile.constraints.Constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class AndConstraint implements GrammaticalConstraint
{
    private final Collection<Constraint> subConstraints;

    public AndConstraint(Collection<Constraint> subConstraints) {
        this.subConstraints = subConstraints;
    }

    public AndConstraint(Constraint... subConstraints) {
        this(Arrays.asList(subConstraints));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AndConstraint otherConstraint = (AndConstraint) o;
        return Objects.equals(subConstraints, otherConstraint.subConstraints);
    }

    @Override
    public int hashCode(){
        return Objects.hash("AND", subConstraints);
    }

    public Collection<Constraint> getSubConstraints() {
        return subConstraints;
    }

    @Override
    public String toString() {
        return String.join(
            " and ",
            this.subConstraints
                .stream()
                .map(Object::toString)
                .collect(Collectors.toSet()));
    }
}

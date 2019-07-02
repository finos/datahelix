Copyright 2019 Scott Logic Ltd /
/
Licensed under the Apache License, Version 2.0 (the \"License\");/
you may not use this file except in compliance with the License./
You may obtain a copy of the License at/
/
    http://www.apache.org/licenses/LICENSE-2.0/
/
Unless required by applicable law or agreed to in writing, software/
distributed under the License is distributed on an \"AS IS\" BASIS,/
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied./
See the License for the specific language governing permissions and/
limitations under the License.
package com.scottlogic.deg.common.profile.constraints.atomic;

import com.scottlogic.deg.common.profile.Field;

import com.scottlogic.deg.common.profile.RuleInformation;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class IsInSetConstraint implements AtomicConstraint {
    public final Field field;
    public final Set<Object> legalValues;
    private final Set<RuleInformation> rules;

    public IsInSetConstraint(Field field, Set<Object> legalValues, Set<RuleInformation> rules) {
        this.field = field;
        this.legalValues = legalValues;
        this.rules = rules;

        if (legalValues.isEmpty()) {
            throw new IllegalArgumentException("Cannot create an IsInSetConstraint for field '" +
                field.name + "' with an empty set.");
        }

        if (legalValues.contains(null)){
            throw new IllegalArgumentException("Cannot create an IsInSetConstraint for field '" +
                field.name + "' with a set containing null.");
        }
    }

    @Override
    public String toDotLabel() {
        final int limit = 3;

        if (legalValues.size() <= limit) {
            return String.format("%s in [%s]", field.name,
                legalValues.stream().map(x -> x.toString()).collect(Collectors.joining(", ")));
        }


        return String.format("%s in [%s, ...](%d values)",
            field.name,
            legalValues.stream().limit(limit).map(x -> x.toString()).collect(Collectors.joining(", ")),
            legalValues.size());
    }

    @Override
    public Field getField() {
        return field;
    }

    public String toString(){
        return String.format(
                "`%s` in %s",
                field.name,
                Objects.toString(legalValues));
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o instanceof ViolatedAtomicConstraint) {
            return o.equals(this);
        }
        if (o == null || getClass() != o.getClass()) return false;
        IsInSetConstraint constraint = (IsInSetConstraint) o;
        return Objects.equals(field, constraint.field) && Objects.equals(legalValues, constraint.legalValues);
    }

    @Override
    public int hashCode(){
        return Objects.hash(field, legalValues);
    }

    @Override
    public Set<RuleInformation> getRules() {
        return rules;
    }

    @Override
    public AtomicConstraint withRules(Set<RuleInformation> rules) {
        return new IsInSetConstraint(this.field, this.legalValues, rules);
    }
}

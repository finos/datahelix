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

package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;
import com.scottlogic.deg.generator.restrictions.TypedRestrictions;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class RestrictionsFieldSpec extends FieldSpec {
    private final TypedRestrictions restrictions;
    private final Set<Object> blacklist;

    RestrictionsFieldSpec(TypedRestrictions restrictions, boolean nullable, Set<Object> blacklist) {
        super(nullable);
        this.restrictions = restrictions;
        this.blacklist = blacklist;
    }

    @Override
    public boolean permits(Object value) {
        return !blacklist.contains(value) && restrictions.match(value);
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return appendNullSource(
            restrictions.createFieldValueSource(blacklist));
    }

    @Override
    public FieldSpec withNotNull() {
        return new RestrictionsFieldSpec(restrictions, false, blacklist);
    }

    public TypedRestrictions getRestrictions() {
        return restrictions;
    }

    public Collection<Object> getBlacklist() {
        return blacklist;
    }

    public RestrictionsFieldSpec withBlacklist(Set<Object> blacklist) {
        return new RestrictionsFieldSpec(restrictions, nullable, blacklist);
    }

    @Override
    public String toString() {
        return String.format("%s%s",
            nullable ? " " : " Not Null ",
            restrictions == null ? "" : restrictions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestrictionsFieldSpec that = (RestrictionsFieldSpec) o;
        return Objects.equals(restrictions, that.restrictions) &&
            Objects.equals(blacklist, that.blacklist) &&
            Objects.equals(nullable, that.nullable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(restrictions, blacklist, nullable);
    }
}

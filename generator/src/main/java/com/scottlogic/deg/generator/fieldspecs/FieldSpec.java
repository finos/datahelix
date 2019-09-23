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

import com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types;

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedSet;
import com.scottlogic.deg.generator.fieldspecs.whitelist.FrequencyDistributedSet;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.scottlogic.deg.common.profile.constraints.atomic.IsOfTypeConstraint.Types.*;

/**
 * Details a column's atomic constraints
 * A fieldSpec can either be a whitelist of allowed values, or a set of restrictions.
 * if a fieldSpec can not be a type, it will not have any restrictions for that type
 * This is enforced during merging.
 */
public class FieldSpec {
    public static final Collection<Types> ALL_TYPES_PERMITTED = Arrays.asList(Types.values());
    public static final FieldSpec Empty = new FieldSpec(null, null, true, Collections.emptySet(), null);
    public static final FieldSpec NullOnly = Empty.withWhitelist(FrequencyDistributedSet.empty());

    private final boolean nullable;
    private final DistributedSet<Object> whitelist;
    private final Set<Object> blacklist;
    private final TypedRestrictions restrictions;
    private final Types types;

    private FieldSpec(
        DistributedSet<Object> whitelist,
        TypedRestrictions restrictions,
        boolean nullable,
        Set<Object> blacklist,
        Types types) {
        this.whitelist = whitelist;
        this.restrictions = restrictions;
        this.nullable = nullable;
        this.blacklist = blacklist;
        this.types = types;
    }

    public boolean isNullable() {
        return nullable;
    }

    public DistributedSet<Object> getWhitelist() {
        return whitelist;
    }

    public Set<Object> getBlacklist() {
        return blacklist;
    }

    public NumericRestrictions getNumericRestrictions() {
        if (types == NUMERIC) {
            return (NumericRestrictions) restrictions;
        }
        return null;
    }

    public StringRestrictions getStringRestrictions() {
        if (types == STRING) {
            return (StringRestrictions) restrictions;
        }
        return null;
    }

    public Collection<Types> getTypeRestrictions() {
        return types == null ? ALL_TYPES_PERMITTED : Collections.singleton(types);
    }

    public DateTimeRestrictions getDateTimeRestrictions() {
        if (types == DATETIME) {
            return (DateTimeRestrictions) restrictions;
        }
        return null;
    }

    public FieldSpec withWhitelist(DistributedSet<Object> whitelist) {
        return new FieldSpec(whitelist, null, nullable, blacklist, types);
    }

    public FieldSpec withNumericRestrictions(NumericRestrictions numericRestrictions) {
        return new FieldSpec(null, numericRestrictions, nullable, blacklist, NUMERIC);
    }

    public FieldSpec withBlacklist(Set<Object> blacklist) {
        return new FieldSpec(whitelist, restrictions, nullable, blacklist, types);
    }

    public FieldSpec withTypeRestrictions(Collection<Types> typeRestrictions) {
        return new FieldSpec(whitelist, restrictions, nullable, blacklist, typeRestrictions == ALL_TYPES_PERMITTED ? null : typeRestrictions.iterator().next());
    }

    public FieldSpec withStringRestrictions(StringRestrictions stringRestrictions) {
        return new FieldSpec(null, stringRestrictions, nullable, blacklist, STRING);
    }

    public FieldSpec withNotNull() {
        return new FieldSpec(whitelist, restrictions, false, blacklist, types);
    }

    public FieldSpec withDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions) {
        return new FieldSpec(null, dateTimeRestrictions, nullable, blacklist, DATETIME);
    }

    public boolean isType(Types type){
        return types == type;
    }

    @Override
    public String toString() {
        if (whitelist != null) {
            if (whitelist.isEmpty()) {
                return "Null only";
            }
            return (nullable ? "" : "Not Null") + String.format("IN %s", whitelist);
        }

        return String.format("%s%s%s",
            types == null  ? "<all values>" : types,
            nullable ? " " : " Not Null ",
            restrictions == null ? "" : restrictions);
    }

    /**
     * Create a predicate that returns TRUE for all (and only) values permitted by this FieldSpec
     */
    public boolean permits(Object value) {
        if (blacklist.contains(value)){
            return false;
        }

        if (types != null && !types.isInstanceOf(value)) {
            return false;
        }

        if (restrictions != null && !restrictions.match(value)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return Objects.hash(nullable, whitelist, restrictions, blacklist, types);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        FieldSpec other = (FieldSpec) obj;
        return Objects.equals(nullable, other.nullable)
            && Objects.equals(whitelist, other.whitelist)
            && Objects.equals(restrictions, other.restrictions)
            && Objects.equals(blacklist, other.blacklist)
            && Objects.equals(types, other.types);
    }
}

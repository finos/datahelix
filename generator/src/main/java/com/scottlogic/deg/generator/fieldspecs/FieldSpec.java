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
import com.scottlogic.deg.common.util.HeterogeneousTypeContainer;

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
    public static final FieldSpec Empty = new FieldSpec(null, new HeterogeneousTypeContainer<>(), true, Collections.emptySet(), ALL_TYPES_PERMITTED);
    public static final FieldSpec NullOnly = Empty.withWhitelist(FrequencyDistributedSet.empty());

    private final boolean nullable;
    private final DistributedSet<Object> whitelist;
    private final Set<Object> blacklist;
    private final HeterogeneousTypeContainer<Restrictions> restrictions;
    private final Collection<Types> types;

    private FieldSpec(
        DistributedSet<Object> whitelist,
        HeterogeneousTypeContainer<Restrictions> restrictions,
        boolean nullable,
        Set<Object> blacklist,
        Collection<Types> types) {
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
        return restrictions.get(NumericRestrictions.class).orElse(null);
    }

    public StringRestrictions getStringRestrictions() {
        return restrictions.get(StringRestrictions.class).orElse(null);
    }

    public Collection<Types> getTypeRestrictions() {
        return types;
    }

    public DateTimeRestrictions getDateTimeRestrictions() {
        return restrictions.get(DateTimeRestrictions.class).orElse(null);
    }

    public FieldSpec withWhitelist(DistributedSet<Object> whitelist) {
        return new FieldSpec(whitelist, new HeterogeneousTypeContainer<>(), nullable, blacklist, types);
    }

    public FieldSpec withNumericRestrictions(NumericRestrictions numericRestrictions) {
        return withConstraint(NumericRestrictions.class, numericRestrictions, NUMERIC);
    }

    public FieldSpec withBlacklist(Set<Object> blacklist) {
        return new FieldSpec(whitelist, restrictions, nullable, blacklist, types);
    }

    public FieldSpec withTypeRestrictions(Collection<Types> typeRestrictions) {
        return new FieldSpec(whitelist, restrictions, nullable, blacklist, typeRestrictions);
    }

    public FieldSpec withStringRestrictions(StringRestrictions stringRestrictions) {
        return withConstraint(StringRestrictions.class, stringRestrictions, STRING);
    }

    public FieldSpec withNotNull() {
        return new FieldSpec(whitelist, restrictions, false, blacklist, types);
    }

    public FieldSpec withDateTimeRestrictions(DateTimeRestrictions dateTimeRestrictions) {
        return withConstraint(DateTimeRestrictions.class, dateTimeRestrictions, DATETIME);
    }

    private <T extends Restrictions> FieldSpec withConstraint(Class<T> type, T restriction, Types constraintType) {
        if (restriction == null){
            return this;
        }
        if (!types.contains(constraintType)){
            throw new UnsupportedOperationException("Cannot give the wrong restriction type to a Field spec");
        }

        Collection<Types> newType = types.size() == 1 ? types : Collections.singleton(constraintType);

        return new FieldSpec(null, restrictions.put(type, restriction), nullable, blacklist, newType);
    }

    public boolean isTypeAllowed(Types type){
        if (whitelist != null){
            return false;
        }
        return types.contains(type);
    }

    @Override
    public String toString() {
        if (whitelist != null) {
            if (whitelist.isEmpty()) {
                return "Null only";
            }
            return (nullable ? "" : "Not Null") + String.format("IN %s", whitelist);
        }

        List<String> propertyStrings = restrictions.values()
                .stream()
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());

        if (propertyStrings.isEmpty()) {
            return "<all values>";
        }

        if (!nullable){
            propertyStrings.add(0, "Not Null");
        }

        return String.join(" & ", propertyStrings);
    }

    /**
     * Create a predicate that returns TRUE for all (and only) values permitted by this FieldSpec
     */
    public boolean permits(Object value) {
        if (blacklist.contains(value)){
            return false;
        }

        Collection<Types> typeRestrictions = getTypeRestrictions();
        if (typeRestrictions != null) {
            for (Types type : Types.values()) {
                if (!typeRestrictions.contains(type) && type.isInstanceOf(value)) {
                    return false;
                }
            }
        }

        Set<Class<? extends Restrictions>> keys = new HashSet<>();
        keys.add(NumericRestrictions.class);
        keys.add(DateTimeRestrictions.class);
        keys.add(StringRestrictions.class);

        Set<TypedRestrictions> toCheckForMatch = restrictions.getMultiple(keys)
            .stream()
            .map(r -> (TypedRestrictions) r)
            .collect(Collectors.toSet());
        for (TypedRestrictions restriction : toCheckForMatch) {
            if (restriction != null && restriction.isInstanceOf(value) && !restriction.match(value)) {
                return false;
            }
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

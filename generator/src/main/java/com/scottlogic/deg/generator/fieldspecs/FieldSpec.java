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

import com.scottlogic.deg.common.profile.FieldType;
import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.restrictions.*;

import java.util.*;

import static com.scottlogic.deg.generator.restrictions.StringRestrictionsFactory.forMaxLength;
import static com.scottlogic.deg.generator.restrictions.linear.LinearRestrictionsFactory.*;

/**
 * Details a column's atomic constraints
 * A fieldSpec can either be a whitelist of allowed values, or a set of restrictions.
 * if a fieldSpec can not be a type, it will not have any restrictions for that type
 * This is enforced during merging.
 */
public class FieldSpec {

    private final boolean nullable;
    private final DistributedList<Object> whitelist;
    private final Set<Object> blacklist;
    private final TypedRestrictions restrictions;

    FieldSpec(
        DistributedList<Object> whitelist,
        TypedRestrictions restrictions,
        boolean nullable,
        Set<Object> blacklist) {
        this.whitelist = whitelist;
        this.restrictions = restrictions;
        this.nullable = nullable;
        this.blacklist = blacklist;
    }

    public boolean isNullable() {
        return nullable;
    }

    public DistributedList<Object> getWhitelist() {
        return whitelist;
    }

    public Set<Object> getBlacklist() {
        return blacklist;
    }

    public TypedRestrictions getRestrictions() {
        return restrictions;
    }

    public FieldSpec withBlacklist(Set<Object> blacklist) {
        return new FieldSpec(whitelist, restrictions, nullable, blacklist);
    }

    public FieldSpec withNotNull() {
        return new FieldSpec(whitelist, restrictions, false, blacklist);
    }

    @Override
    public String toString() {
        if (whitelist != null) {
            if (whitelist.isEmpty()) {
                return "Null only";
            }
            return (nullable ? "" : "Not Null ") + String.format("IN %s", whitelist);
        }

        return String.format("%s%s",
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

        if (restrictions != null && !restrictions.match(value)) {
            return false;
        }

        if (whitelist != null && !whitelist.list().contains(value)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return Objects.hash(nullable, whitelist, restrictions, blacklist);
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
            && Objects.equals(blacklist, other.blacklist);
    }
}

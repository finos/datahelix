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

import com.scottlogic.deg.generator.fieldspecs.whitelist.DistributedList;
import com.scottlogic.deg.generator.generation.fieldvaluesources.CannedValuesFieldValueSource;
import com.scottlogic.deg.generator.generation.fieldvaluesources.FieldValueSource;

import java.util.Objects;

public class WhitelistFieldSpec extends FieldSpec {

    private final DistributedList<Object> whitelist;

    WhitelistFieldSpec(DistributedList<Object> whitelist, boolean nullable) {
        super(nullable);
        if (whitelist.isEmpty()){
            throw new UnsupportedOperationException("cannot create with empty whitelist");
        }
        this.whitelist = whitelist;
    }

    @Override
    public boolean permits(Object value) {
        if (!whitelist.list().contains(value)) {
            return false;
        }

        return true;
    }

    @Override
    public FieldValueSource getFieldValueSource() {
        return appendNullSource(new CannedValuesFieldValueSource(whitelist));
    }

    @Override
    public WhitelistFieldSpec withNotNull() {
        return new WhitelistFieldSpec(whitelist, false);
    }

    public DistributedList<Object> getWhitelist() {
        return whitelist;
    }

    @Override
    public String toString() {
        if (whitelist.isEmpty()) {
            return "Null only";
        }
        return (nullable ? "" : "Not Null ") + String.format("IN %s", whitelist);
    }

    public int hashCode() {
        return Objects.hash(nullable, whitelist);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        WhitelistFieldSpec other = (WhitelistFieldSpec) obj;
        return Objects.equals(nullable, other.nullable)
            && Objects.equals(whitelist, other.whitelist);
    }
}

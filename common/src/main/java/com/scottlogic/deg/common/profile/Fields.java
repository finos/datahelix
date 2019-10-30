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



import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class Fields implements Iterable<Field> {
    private final List<Field> fields;

    public Fields(List<Field> fields) {
        this.fields = fields;
    }

    public Field getByName(String fieldName) {
        return this.fields.stream()
            .filter(f -> f.getName().equals(fieldName))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Profile fields do not contain " + fieldName));
    }
    
    public int size() {
        return this.fields.size();
    }

    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }

    public Stream<Field> stream() {
        return this.fields.stream();
    }

    public Stream<Field> getExternalStream() {
        return this.stream().filter(f -> !f.isInternal());
    }

    public List<Field> asList() {
        return fields;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }

        Fields fields = (Fields) obj;
        return this.fields.equals(fields.fields);
    }

    @Override
    public int hashCode() {
        return fields.hashCode();
    }
}

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

import java.util.Objects;

public class Field {
    public final String name;
    private final boolean unique;
    private final String formatting;

    public Field(String name) {
        this(name, false, null);
    }

    public Field(String name, Boolean unique, String formatting) {
        this.name = name;
        this.unique = unique;
        this.formatting = formatting;
    }

    public boolean isUnique() {
        return unique;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(name, field.name)
            && Objects.equals(unique, field.unique)
            && Objects.equals(formatting, field.formatting);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, unique, formatting);
    }

    public String getFormatting() {
        return formatting;
    }
}

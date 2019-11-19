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

package com.scottlogic.datahelix.generator.common.profile;

import java.util.Objects;

public class SpecificFieldType {
    private final String type;
    private final FieldType fieldType;
    private final String formatting;
    private final String fakerMethod;

    public SpecificFieldType(String type, FieldType fieldType, String formatting) {
        this(type, fieldType, formatting, null);
    }

    public SpecificFieldType(String type, FieldType fieldType, String formatting, String fakerMethod) {
        this.type = type;
        this.fieldType = fieldType;
        this.formatting = formatting;
        this.fakerMethod = fakerMethod;
    }

    public String getType() {
        return type;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public String getFormatting() {
        return formatting;
    }

    public String getFakerMethod() {
        return fakerMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecificFieldType that = (SpecificFieldType) o;
        return Objects.equals(type, that.type) &&
            fieldType == that.fieldType &&
            Objects.equals(formatting, that.formatting) &&
            Objects.equals(fakerMethod, that.fakerMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, fieldType, formatting, fakerMethod);
    }
}

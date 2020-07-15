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

import java.util.Arrays;
import java.util.Optional;

import static com.scottlogic.datahelix.generator.common.util.Defaults.DEFAULT_DATE_FORMATTING;
import static com.scottlogic.datahelix.generator.common.util.Defaults.DEFAULT_TIME_FORMATTING;

public enum StandardSpecificFieldType {
    DECIMAL("decimal", FieldType.NUMERIC, null),
    INTEGER( "integer", FieldType.NUMERIC, null),
    ISIN("ISIN", FieldType.STRING, null),
    SEDOL("SEDOL", FieldType.STRING, null),
    CUSIP("CUSIP", FieldType.STRING, null),
    RIC("RIC", FieldType.STRING, null),
    FIRST_NAME("firstname", FieldType.STRING, null),
    LAST_NAME("lastname", FieldType.STRING, null),
    FULL_NAME("fullname", FieldType.STRING, null),
    STRING("string", FieldType.STRING, null),
    DATETIME("datetime", FieldType.DATETIME, null),
    DATE("date",FieldType.DATETIME, DEFAULT_DATE_FORMATTING),
    BOOLEAN("boolean", FieldType.BOOLEAN, null),
    TIME("time", FieldType.TIME, DEFAULT_TIME_FORMATTING),
    FAKER("faker.", FieldType.STRING, null);

    private final String type;
    private final FieldType fieldType;
    private final String defaultFormatting;

    StandardSpecificFieldType(String type, FieldType fieldType, String defaultFormatting) {
        this.type = type;
        this.fieldType = fieldType;
        this.defaultFormatting = defaultFormatting;
    }

    public String getType() {
        return type;
    }

    public FieldType getFieldType()
    {
        return fieldType;
    }

    public static Optional<StandardSpecificFieldType> from(String type) {
        return Arrays.stream(StandardSpecificFieldType.values())
            .filter(sft -> sft.type.equals(type))
            .findAny();
    }

    public String getDefaultFormatting() {
        return defaultFormatting;
    }

    public SpecificFieldType toSpecificFieldType() {
        return new SpecificFieldType(type, fieldType, getDefaultFormatting());
    }
}

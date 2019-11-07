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

import com.fasterxml.jackson.annotation.JsonValue;

import static com.scottlogic.deg.common.util.Defaults.DEFAULT_DATE_FORMATTING;

public enum SpecificFieldType
{
    DECIMAL("decimal", FieldType.NUMERIC),
    INTEGER( "integer", FieldType.NUMERIC),
    ISIN("ISIN", FieldType.STRING),
    SEDOL("SEDOL", FieldType.STRING),
    CUSIP("CUSIP", FieldType.STRING),
    RIC("RIC", FieldType.STRING),
    FIRST_NAME("firstname", FieldType.STRING),
    LAST_NAME("lastname", FieldType.STRING),
    FULL_NAME("fullname", FieldType.STRING),
    STRING("string", FieldType.STRING),
    DATETIME("datetime", FieldType.DATETIME),
    DATE("date",FieldType.DATETIME);

    @JsonValue
    private final String type;
    private final FieldType fieldType;

    SpecificFieldType(String type, FieldType fieldType)
    {
        this.type = type;
        this.fieldType = fieldType;
    }

    public String getType() {
        return type;
    }

    public FieldType getFieldType()
    {
        return fieldType;
    }

    public static SpecificFieldType from(String type){
        switch (type)
        {
            case "decimal": return DECIMAL;
            case "integer": return INTEGER;
            case "ISIN": return ISIN;
            case "SEDOL": return SEDOL;
            case "CUSIP": return CUSIP;
            case "RIC": return RIC;
            case "firstname": return FIRST_NAME;
            case "lastname": return LAST_NAME;
            case "fullname": return FULL_NAME;
            case "string": return STRING;
            case "datetime": return DATETIME;
            case "date": return DATE;
            default:
                throw new IllegalStateException("No data types with type " + type);
        }
    }

    public String getDefaultFormatting() {
        switch (type) {
            case "date": return DEFAULT_DATE_FORMATTING;
            default:
                return null;
        }
    }
}

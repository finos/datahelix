package com.scottlogic.deg.common.profile;

import com.fasterxml.jackson.annotation.JsonValue;

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
}

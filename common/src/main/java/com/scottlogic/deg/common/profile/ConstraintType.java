package com.scottlogic.deg.common.profile;

public enum ConstraintType
{
    EQUAL_TO("equalTo"),
    IN_SET("inSet"),
    IN_MAP("inMap"),
    NULL("null"),
    GRANULAR_TO("granularTo"),
    MATCHES_REGEX("matchingRegex"),
    CONTAINS_REGEX("containingRegex"),
    OF_LENGTH("ofLength"),
    LONGER_THAN("longerThan"),
    SHORTER_THAN("shorterThan"),
    GREATER_THAN("greaterThan"),
    GREATER_THAN_OR_EQUAL_TO("greaterThanOrEqualTo"),
    LESS_THAN("lessThan"),
    LESS_THAN_OR_EQUAL_TO("lessThanOrEqualTo"),
    AFTER("after"),
    AFTER_OR_AT("afterOrAt"),
    BEFORE("before"),
    BEFORE_OR_AT("beforeOrAt"),
    NOT("not"),
    ANY_OF("anyOf"),
    ALL_OF("allOf"),
    CONDITION("condition");

    private final String type;

    ConstraintType(String name)
    {
        this.type = name;
    }

    public String getType()
    {
        return type;
    }

    public static ConstraintType fromString(String type){
        switch (type)
        {
            case "equalTo": return EQUAL_TO;
            case "inSet": return IN_SET;
            case "inMap": return IN_MAP;
            case "null": return NULL;
            case "granularTo": return GRANULAR_TO;
            case "matchingRegex": return MATCHES_REGEX;
            case "containingRegex": return CONTAINS_REGEX;
            case "ofLength": return OF_LENGTH;
            case "longerThan": return LONGER_THAN;
            case "shorterThan": return SHORTER_THAN;
            case "greaterThan": return GREATER_THAN;
            case "greaterThanOrEqualTo": return GREATER_THAN_OR_EQUAL_TO;
            case "lessThan": return LESS_THAN;
            case "lessThanOrEqualTo": return LESS_THAN_OR_EQUAL_TO;
            case "after": return  AFTER;
            case "afterOrAt": return AFTER_OR_AT;
            case "before": return BEFORE;
            case "beforeOrAt": return BEFORE_OR_AT;
            case "not": return NOT;
            case "anyOf": return ANY_OF;
            case "allOf": return ALL_OF;
            case "condition": return CONDITION;
            default:
                throw new IllegalStateException("No constraint types with name " + type);
        }
    }
}

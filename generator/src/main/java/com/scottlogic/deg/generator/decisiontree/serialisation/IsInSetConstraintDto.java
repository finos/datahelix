package com.scottlogic.deg.generator.decisiontree.serialisation;

import java.util.List;

public class IsInSetConstraintDto implements ConstraintDto {
    public FieldDto field;
    public List<Object> legalValues;
}

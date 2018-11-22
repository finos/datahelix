package com.scottlogic.deg.generator.decisiontree.ser_deser;

import java.util.Collection;

public class ConstraintNodeDto {
    public Collection<ConstraintDto> atomicConstraints;
    public Collection<DecisionNodeDto> decisions;
}

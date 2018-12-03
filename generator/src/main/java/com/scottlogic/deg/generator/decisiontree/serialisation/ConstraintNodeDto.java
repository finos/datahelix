package com.scottlogic.deg.generator.decisiontree.serialisation;

import java.util.Collection;

public class ConstraintNodeDto {
    public Collection<ConstraintDto> atomicConstraints;
    public Collection<DecisionNodeDto> decisions;
}

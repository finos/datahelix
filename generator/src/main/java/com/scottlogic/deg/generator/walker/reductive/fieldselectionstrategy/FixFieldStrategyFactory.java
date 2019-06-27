package com.scottlogic.deg.generator.walker.reductive.fieldselectionstrategy;

import com.scottlogic.deg.generator.decisiontree.ConstraintNode;

public class FixFieldStrategyFactory {
    public FixFieldStrategy create(ConstraintNode rootNode){
        return new FieldAppearanceFixingStrategy(rootNode);
    }
}
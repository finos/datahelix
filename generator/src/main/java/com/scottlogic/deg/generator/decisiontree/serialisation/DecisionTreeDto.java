package com.scottlogic.deg.generator.decisiontree.serialisation;

import com.scottlogic.deg.schemas.v3.V3ProfileDTO;

/**
 * Data transfer object for a Decision Tree.
 */
public class DecisionTreeDto {
    public ConstraintNodeDto rootNode;
    public V3ProfileDTO profile;
    public String description;
}

package com.scottlogic.deg.generator.fieldspecs;

import com.scottlogic.deg.common.profile.Field;

public interface FieldSpecRelations {

    /**
     * Produce a field spec similar to how atomic constraints create fieldspecs.
     * This fieldspec should contain a barebones set of restrictions, with only the
     * implementation specific bound included.
     *
     * An example would be for (a:Int > b:Int), where a and b have many restrictions across different types.
     * The resultant fieldspec would take the fieldspec of a, and extract ONLY the RELEVANT Int information:
     *
     * a:Int maximum value, which is the maximum bound of a minus a unit (for ints, 1).
     * This ensures that the resultant fieldspec, when merged with b, will guarantee that a can choose a value
     * that respects the inputted relational constraint.
     *
     * @param otherValue
     * @return
     */
    FieldSpec reduceToRelatedFieldSpec(FieldSpec otherValue);

    Field other();

}

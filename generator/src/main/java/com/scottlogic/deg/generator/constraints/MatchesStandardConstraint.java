package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.generation.IStringGenerator;

public class MatchesStandardConstraint implements IConstraint {
    public final Field field;
    public final IStringGenerator standard;

    public MatchesStandardConstraint(Field field, IStringGenerator standard) {
        this.field = field;
        this.standard = standard;
    }
}

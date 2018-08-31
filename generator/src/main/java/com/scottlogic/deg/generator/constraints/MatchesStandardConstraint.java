package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;
import com.scottlogic.deg.generator.utils.IStringGenerator;

import java.util.regex.Pattern;

public class MatchesStandardConstraint implements IConstraint
{
    public final Field field;
    public final IStringGenerator standard;

    public MatchesStandardConstraint(Field field, IStringGenerator standard) {
        this.field = field;
        this.standard = standard;
    }
}

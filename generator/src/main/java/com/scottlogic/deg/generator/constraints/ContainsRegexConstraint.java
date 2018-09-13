package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.regex.Pattern;

public class ContainsRegexConstraint implements IConstraint
{
    public final Field field;
    public final Pattern regex;

    public ContainsRegexConstraint(Field field, Pattern regex) {
        this.field = field;
        this.regex = regex;
    }
}

package com.scottlogic.deg.generator.constraints;

import com.scottlogic.deg.generator.Field;

import java.util.regex.Pattern;

public class MatchesRegexConstraint implements IConstraint
{
    public final Field field;
    public final Pattern regex;

    public MatchesRegexConstraint(Field field, Pattern regex) {
        this.field = field;
        this.regex = regex;
    }

    @Override
    public String toString(){
        return String.format("%s matches '%s'", field.name, regex);
    }
}

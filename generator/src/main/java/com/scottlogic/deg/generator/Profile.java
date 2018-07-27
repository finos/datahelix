package com.scottlogic.deg.generator;

import java.util.Collection;

public class Profile
{
    public final Collection<Field> fields;
    public final Collection<Rule> rules;

    public Profile(Collection<Field> fields, Collection<Rule> rules) {
        this.fields = fields;
        this.rules = rules;
    }
}

package com.scottlogic.deg.generator;

import java.util.Collection;
import java.util.List;

public class Profile
{
    public final ProfileFields fields;
    public final Collection<Rule> rules;

    public Profile(List<Field> fields, Collection<Rule> rules) {
        this.fields = new ProfileFields(fields);
        this.rules = rules;
    }
}

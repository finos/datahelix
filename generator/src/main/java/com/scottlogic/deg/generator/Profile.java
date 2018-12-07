package com.scottlogic.deg.generator;

import com.scottlogic.deg.generator.inputs.visitor.IProfileVisitor;

import java.util.Collection;
import java.util.List;

public class Profile  {
    public final ProfileFields fields;
    public final Collection<Rule> rules;
    public final String description;

    public Profile(List<Field> fields, Collection<Rule> rules) {
        this(new ProfileFields(fields), rules, null);
    }

    public Profile(List<Field> fields, Collection<Rule> rules, String description) {
        this(new ProfileFields(fields), rules, description);
    }

    public Profile(ProfileFields fields, Collection<Rule> rules) {
        this(fields, rules, null);
    }

    public Profile(ProfileFields fields, Collection<Rule> rules, String description) {
        this.fields = fields;
        this.rules = rules;
        this.description = description;


    }

    public void accept(IProfileVisitor visitor){
        visitor.visit(rules);
    }
}

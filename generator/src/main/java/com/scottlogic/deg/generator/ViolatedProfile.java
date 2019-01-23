package com.scottlogic.deg.generator;

import java.util.Collection;

public class ViolatedProfile extends Profile {
    public final Rule violatedRule;

    public ViolatedProfile(Rule violatedRule, ProfileFields fields, Collection<Rule> rules, String description){
        super(fields, rules, description);
        this.violatedRule = violatedRule;
    }

}

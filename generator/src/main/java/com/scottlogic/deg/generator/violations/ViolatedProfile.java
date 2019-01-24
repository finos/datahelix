package com.scottlogic.deg.generator.violations;

import com.scottlogic.deg.generator.Profile;
import com.scottlogic.deg.generator.ProfileFields;
import com.scottlogic.deg.generator.Rule;

import java.util.Collection;

public class ViolatedProfile extends Profile {
    public final Rule violatedRule;

    public ViolatedProfile(Rule violatedRule, ProfileFields fields, Collection<Rule> rules, String description){
        super(fields, rules, description);
        this.violatedRule = violatedRule;
    }

}

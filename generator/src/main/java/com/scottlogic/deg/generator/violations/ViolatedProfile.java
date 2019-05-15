package com.scottlogic.deg.generator.violations;

import com.scottlogic.deg.common.profile.Profile;
import com.scottlogic.deg.common.profile.ProfileFields;
import com.scottlogic.deg.common.profile.Rule;

import java.util.Collection;

public class ViolatedProfile extends Profile {
    /**
     * Original (un-violated) form of the rule that has been violated in this profile.
     */
    public final Rule violatedRule;

    /**
     * Constructs a new violated profile using the base profile constructor.
     * @param violatedRule Un-violated form of the rule that has been violated on this profile.
     * @param fields Fields relating to this profile.
     * @param rules Collection of rules on this profile, including the violated form of the one rule which has been
     *              violated.
     * @param description Description of profile.
     */
    public ViolatedProfile(Rule violatedRule, ProfileFields fields, Collection<Rule> rules, String description){
        super(fields, rules, description);
        this.violatedRule = violatedRule;
    }

}

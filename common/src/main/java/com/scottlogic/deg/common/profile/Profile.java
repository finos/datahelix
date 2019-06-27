package com.scottlogic.deg.common.profile;

import java.util.Collection;
import java.util.List;

public class Profile {
    private final ProfileFields fields;
    private final Collection<Rule> rules;
    private final String description;

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

    public ProfileFields getFields() {
        return fields;
    }

    public Collection<Rule> getRules() {
        return rules;
    }

    public String getDescription() {
        return description;
    }
}

package com.scottlogic.deg.generator.inputs;

import com.scottlogic.deg.generator.Rule;

public interface RuleViolator {
    /**
     * Given a rule will produce a violated version of this same rule.
     * @param rule Input rule to be violated
     * @return Violated version of the rule.
     */
    Rule violateRule(Rule rule);
}

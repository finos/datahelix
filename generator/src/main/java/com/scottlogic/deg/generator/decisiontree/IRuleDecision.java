package com.scottlogic.deg.generator.decisiontree;

import java.util.Collection;

public interface IRuleDecision {
    Collection<IRuleOption> getOptions();
}

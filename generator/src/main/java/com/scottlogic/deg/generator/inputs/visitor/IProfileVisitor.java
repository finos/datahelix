package com.scottlogic.deg.generator.inputs.visitor;

import com.scottlogic.deg.generator.Rule;

import java.util.Collection;

public interface IProfileVisitor {

    void visit(Collection<Rule> rules);
}

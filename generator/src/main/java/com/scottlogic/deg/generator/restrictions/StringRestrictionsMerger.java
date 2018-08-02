package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.restrictions.StringRestrictions;
import dk.brics.automaton.Automaton;

/**
 * For a given combination of choices over the decision tree
 * Details every column's atomic constraints
 */
public class StringRestrictionsMerger {
    public StringRestrictions merge(StringRestrictions left, StringRestrictions right) {
        if (left == null && right == null)
            return null;
        if (left == null)
            return right;
        if (right == null)
            return left;

        final StringRestrictions merged = new StringRestrictions();
        merged.automaton = getMergedAutomaton(left.automaton, right.automaton);

        return merged;
    }

    private Automaton getMergedAutomaton(Automaton left, Automaton right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        return left.intersection(right);
    }
}

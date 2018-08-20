package com.scottlogic.deg.generator.generation.iterators;

import com.scottlogic.deg.generator.utils.IStringGenerator;
import com.scottlogic.deg.generator.utils.StringGenerator;
import dk.brics.automaton.Automaton;

import java.util.*;

public class StringIterator implements IFieldSpecIterator {

    IStringGenerator automaton;
    Iterator<String> iterator;

    public StringIterator(IStringGenerator automaton, Set<Object> blacklist) {

        if (blacklist != null && blacklist.size() > 0) {
            StringGenerator blacklistAutomaton = new StringGenerator(getBlacklistAutomaton(blacklist));

            this.automaton = automaton.intersect(blacklistAutomaton);
        } else {
            this.automaton = automaton;
        }

        this.iterator = this.automaton.generateAllValues();
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public Object next() {
        return this.iterator.next();
    }

    private Automaton getBlacklistAutomaton(Set<Object> blacklist) {
        String[] blacklistStrings = new String[blacklist.size()];
        int i = 0;
        for (Object obj : blacklist) {
            blacklistStrings[i] = obj.toString();
        }
        return Automaton.makeStringUnion(blacklistStrings).complement();
    }

    @Override
    public boolean isInfinite() {
        return !automaton.IsFinite();
    }


}

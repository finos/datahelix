/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.scottlogic.deg.generator.generation.string.generators;

import com.scottlogic.deg.generator.generation.string.AutomatonUtils;
import com.scottlogic.deg.generator.generation.string.iterators.FiniteStringAutomatonIterator;
import com.scottlogic.deg.generator.generation.string.factorys.InterestingStringFactory;
import com.scottlogic.deg.generator.generation.string.factorys.RandomStringFactory;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import dk.brics.automaton.Automaton;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RegexStringGenerator implements StringGenerator {

    /**
     * Cache of all matching regex automatons, keyed on their regex
     */
    private static final Map<String, Automaton> matchingRegexAutomatonCache = new HashMap<>();

    /**
     * Cache of all containing regex automatons, keyed on their regex
     */
    private static final Map<String, Automaton> containingRegexAutomatonCache = new HashMap<>();

    private Automaton automaton;
    private final String regexRepresentation;

    private RandomStringFactory randomStringFactory = new RandomStringFactory();
    private InterestingStringFactory interestingStringFactory = new InterestingStringFactory();

    private RegexStringGenerator(Automaton automaton, String regexRepresentation) {
        this.automaton = automaton;
        this.regexRepresentation = regexRepresentation;
    }

    public RegexStringGenerator(String regexStr, boolean matchFullString) {
        Map<String, Automaton> cache = matchFullString ? matchingRegexAutomatonCache : containingRegexAutomatonCache;
        Automaton generatedAutomaton = cache.containsKey(regexStr)
            ? cache.get(regexStr)
            : AutomatonUtils.createAutomaton(regexStr, matchFullString, cache);

        String prefix = matchFullString ? "" : "*";
        String suffix = matchFullString ? "" : "*";
        this.regexRepresentation = String.format("%s/%s/%s", prefix, regexStr, suffix);
        this.automaton = generatedAutomaton;
    }

    @Override
    public String toString() {
        if (regexRepresentation != null) {
            return regexRepresentation;
        }

        if (this.automaton != null) {
            return this.automaton.toString();
        }

        return "<UNKNOWN>";
    }

    public static RegexStringGenerator createFromBlacklist(Set<String> blacklist) {
        String[] blacklistStrings = blacklist.stream().toArray(String[]::new);
        Automaton automaton = Automaton.makeStringUnion(blacklistStrings).complement();

        return new RegexStringGenerator(automaton, String.format("NOT-IN %s", Objects.toString(blacklist)));
    }

    @Override
    public StringGenerator intersect(StringGenerator otherGenerator) {
        if (otherGenerator instanceof NoStringsStringGenerator) {
            return otherGenerator.intersect(this);
        }

        if (!(otherGenerator instanceof RegexStringGenerator)) {
            return otherGenerator.intersect(this);
        }

        RegexStringGenerator otherRegexGenerator = (RegexStringGenerator) otherGenerator;
        Automaton b = otherRegexGenerator.automaton;
        Automaton merged = automaton.intersection(b);
        if (merged.isEmpty()){
            return new NoStringsStringGenerator("regex combination was contradictory");
        }

        String mergedRepresentation = intersectRepresentation(
            this.regexRepresentation,
            ((RegexStringGenerator)otherGenerator).regexRepresentation);

        return new RegexStringGenerator(merged, mergedRepresentation);
    }

    RegexStringGenerator union(RegexStringGenerator otherGenerator) {
        Automaton b = otherGenerator.automaton;
        Automaton merged = automaton.union(b);
        String mergedRepresentation = unionRepresentation(
            this.regexRepresentation,
            otherGenerator.regexRepresentation
        );
        return new RegexStringGenerator(merged, mergedRepresentation);
    }

    @Override
    public StringGenerator complement() {
        return new RegexStringGenerator(
            this.automaton.clone().complement(),
            complementaryRepresentation(this.regexRepresentation));
    }

    private static String complementaryRepresentation(String representation) {
        return String.format("¬(%s)", representation);
    }

    static String intersectRepresentation(String left, String right) {
        return String.format("(%s ∩ %s)", left, right);
    }

    static String unionRepresentation(String left, String right) {
        return String.format("(%s ∪ %s)", left, right);
    }

    @Override
    public Stream<String> generateInterestingValues() {
        return StreamSupport.stream(interestingStringFactory.generateInterestingValues(automaton).spliterator(), false);
    }

    @Override
    public Stream<String> generateAllValues() {
        Iterator<String> iterator = new FiniteStringAutomatonIterator(automaton);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.SORTED), false);
    }

    @Override
    public Stream<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return Stream.generate(
            () -> randomStringFactory.createRandomString(
                "",
                automaton.getInitialState(),
                1,
                Integer.MAX_VALUE,
                randomNumberGenerator));
    }

    public boolean matches(String subject) {
        return automaton.run(subject);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RegexStringGenerator constraint = (RegexStringGenerator) o;
        return this.automaton.equals(constraint.automaton);
    }

    public int hashCode() {
        return Objects.hash(this.automaton, this.getClass());
    }
}


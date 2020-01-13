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

package com.scottlogic.datahelix.generator.core.generation.string.generators;

import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;
import com.scottlogic.datahelix.generator.common.profile.FieldType;
import com.scottlogic.datahelix.generator.core.fieldspecs.FieldSpecFactory;
import com.scottlogic.datahelix.generator.core.generation.string.AutomatonUtils;
import com.scottlogic.datahelix.generator.core.generation.string.factorys.InterestingStringFactory;
import com.scottlogic.datahelix.generator.core.generation.string.factorys.RandomStringFactory;
import com.scottlogic.datahelix.generator.core.generation.string.iterators.FiniteStringAutomatonIterator;
import com.scottlogic.datahelix.generator.core.restrictions.string.StringRestrictions;
import dk.brics.automaton.Automaton;

import java.util.*;
import java.util.stream.Collectors;
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

    private static final RegexStringGenerator DEFAULT = (RegexStringGenerator) ((StringRestrictions) FieldSpecFactory.fromType(FieldType.STRING).getRestrictions()).createGenerator();

    private Automaton automaton;

    private RandomStringFactory randomStringFactory = new RandomStringFactory();
    private InterestingStringFactory interestingStringFactory = new InterestingStringFactory();
    private final RegexPattern regexPattern;

    private RegexStringGenerator(Automaton automaton, RegexPattern regexPattern) {
        this.automaton = automaton;
        this.regexPattern = regexPattern; //clone the list to make it immutable
    }

    public RegexStringGenerator(String regexStr, boolean matchFullString) {
        Map<String, Automaton> cache = matchFullString ? matchingRegexAutomatonCache : containingRegexAutomatonCache;
        Automaton generatedAutomaton = cache.containsKey(regexStr)
            ? cache.get(regexStr)
            : AutomatonUtils.createAutomaton(regexStr, matchFullString, cache);

        String prefix = matchFullString ? "" : "*";
        String suffix = matchFullString ? "" : "*";
        this.regexPattern = new SingleRegexPattern(String.format("%s/%s/%s", prefix, regexStr, suffix));
        this.automaton = generatedAutomaton;
    }

    @Override
    public String toString() {
        String representation = regexPattern.getRepresentation();

        if (representation != null && !representation.equals("")) {
            return representation;
        }

        if (this.automaton != null) {
            return this.automaton.toString();
        }

        return "<UNKNOWN>";
    }

    public static RegexStringGenerator createFromBlacklist(Set<String> blacklist) {
        String[] blacklistStrings = blacklist.toArray(new String[0]);
        Automaton automaton = Automaton.makeStringUnion(blacklistStrings).complement();
        List<RegexPattern> constraints = blacklist.stream()
            .map(SingleRegexPattern::new)
            .collect(Collectors.toList());

        return new RegexStringGenerator(automaton, new NegatedRegexPattern(new AnyRegexPatterns(constraints)));
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
        if (merged.isEmpty()) {
            return new NoStringsStringGenerator("regex combination was contradictory");
        }

        RegexPattern intersectedPatterns = new AllRegexPatterns(
            Arrays.asList(
                this.regexPattern,
                otherRegexGenerator.regexPattern));

        return new RegexStringGenerator(merged, intersectedPatterns);
    }

    @Override
    public StringGenerator complement() {
        return new RegexStringGenerator(
            this.automaton.clone().complement().intersection(DEFAULT.automaton),
            this.regexPattern.complement());
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
                automaton.getInitialState(),
                randomNumberGenerator));
    }

    public boolean validate(String input) {
        return this.regexPattern.matches(input);
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


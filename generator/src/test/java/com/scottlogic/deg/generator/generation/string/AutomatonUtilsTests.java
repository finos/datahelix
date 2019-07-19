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

package com.scottlogic.deg.generator.generation.string;

import dk.brics.automaton.Automaton;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AutomatonUtilsTests {
    
    @Test
    public void getLongestExample_withFixedLengthRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{9}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('a', 9);
        assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,9}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('a', 9);
        assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withLongRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,1000}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('a', 1000);
        assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withSimpleRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[f]{0,10}([a-c]{3}|[d-e]{5})");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('f', 10) + repeat('d', 5);
        assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withComplexRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,10}([a-c]{3}|[d-e]{5})");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        assertThat(longestExample, matchesPattern("[a-z]{10}[d-e]{5}"));
    }

    @Test
    public void getLongestExample_withLengthLimitedEmailAddress_shouldReturnCorrectStringWithLowestOrdinalCharPerSection(){
        Automaton email = getAutomaton("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$");
        Automaton lengthRestriction = getAutomaton("^.{0,20}$");
        Automaton intersected = email.intersection(lengthRestriction);

        String longestExample = AutomatonUtils.getLongestExample(intersected);

        assertThat(longestExample.length(), equalTo(20));
        assertThat(longestExample, matchesPattern("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$"));
    }

    @Test
    public void getLongestExample_withEmptyString_shouldReturnEmptyString(){
        Automaton automaton = getAutomaton("[a-z]{0}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        assertThat(longestExample, equalTo(""));
    }

    @Test
    public void getShortestExample_withFixedLengthRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{9}");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        String expected = repeat('a', 9);
        assertThat(shortestExample, equalTo(expected));
    }

    @Test
    public void getShortestExample_withRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,9}");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        assertThat(shortestExample, equalTo(""));
    }

    @Test
    public void getShortestExample_withLongRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,1000}");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        assertThat(shortestExample, equalTo(""));
    }

    @Test
    public void getShortestExample_withSimpleRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[f]{0,10}([a-c]{3}|[d-e]{5})");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        String expected = repeat('a', 3);
        assertThat(shortestExample, equalTo(expected));
    }

    @Test
    public void getShortestExample_withComplexRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,10}([a-c]{3}|[d-e]{5})");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        assertThat(shortestExample, matchesPattern("[a-z]{3}"));
    }

    @Test
    public void getShortestExample_withLengthLimitedEmailAddress_shouldReturnCorrectStringWithLowestOrdinalCharPerSection(){
        Automaton email = getAutomaton("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$");
        Automaton lengthRestriction = getAutomaton("^.{0,20}$");
        Automaton intersected = email.intersection(lengthRestriction);

        String longestExample = AutomatonUtils.getShortestExample(intersected);

        assertThat(longestExample.length(), equalTo(7));
        assertThat(longestExample, matchesPattern("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$"));
    }

    @Test
    public void getShortestExample_withEmptyString_shouldReturnEmptyString(){
        Automaton automaton = getAutomaton("[a-z]{0}");

        String longestExample = AutomatonUtils.getShortestExample(automaton);

        assertThat(longestExample, equalTo(""));
    }

    @Test
    public void createAutomaton_withValidString_shouldAcceptValidCharacters(){
        String validRegex = ".*";
        Map<String, Automaton> dummyCache = new HashMap<>();

        Automaton automaton = AutomatonUtils.createAutomaton(validRegex, true, dummyCache);

        assertTrue(automaton.run("a"));
    }

    @Test
    public void createAutomaton_withValidString_shouldRejectInvalidCharacters(){
        String validRegex = ".*";
        Map<String, Automaton> dummyCache = new HashMap<>();

        Automaton automaton = AutomatonUtils.createAutomaton(validRegex, true, dummyCache);

        assertFalse(automaton.run("汉字"));
    }

    @Test
    public void createAutomaton_withInValidString_shouldCreateEmptyAutomaton(){
        String validRegex = "汉字*";
        Map<String, Automaton> dummyCache = new HashMap<>();

        Automaton automaton = AutomatonUtils.createAutomaton(validRegex, true, dummyCache);

        assertTrue(automaton.isEmpty());
    }

    private static Automaton getAutomaton(String regex){
        return AutomatonUtils.createAutomaton(regex, true, new HashMap<>());
    }

    private static String repeat(Character character, int times){
        return IntStream.range(0, times).mapToObj(index -> character.toString()).collect(Collectors.joining(""));
    }
}
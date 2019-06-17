package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.generation.string.AutomatonUtils;
import com.scottlogic.deg.generator.generation.string.RegexStringGenerator;
import dk.brics.automaton.Automaton;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.core.IsEqual.equalTo;

class AutomatonUtilsTests {
    @Test
    public void getLongestExample_withFixedLengthRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{9}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('a', 9);
        Assert.assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,9}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('a', 9);
        Assert.assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withLongRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,1000}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('a', 1000);
        Assert.assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withSimpleRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[f]{0,10}([a-c]{3}|[d-e]{5})");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        String expected = repeat('f', 10) + repeat('d', 5);
        Assert.assertThat(longestExample, equalTo(expected));
    }

    @Test
    public void getLongestExample_withComplexRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,10}([a-c]{3}|[d-e]{5})");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        Assert.assertThat(longestExample, matchesPattern("[a-z]{10}[d-e]{5}"));
    }

    @Test
    public void getLongestExample_withLengthLimitedEmailAddress_shouldReturnCorrectStringWithLowestOrdinalCharPerSection(){
        Automaton email = getAutomaton("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$");
        Automaton lengthRestriction = getAutomaton("^.{0,20}$");
        Automaton intersected = email.intersection(lengthRestriction);

        String longestExample = AutomatonUtils.getLongestExample(intersected);

        Assert.assertThat(longestExample.length(), equalTo(20));
        Assert.assertThat(longestExample, matchesPattern("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$"));
    }

    @Test
    public void getLongestExample_withEmptyString_shouldReturnEmptyString(){
        Automaton automaton = getAutomaton("[a-z]{0}");

        String longestExample = AutomatonUtils.getLongestExample(automaton);

        Assert.assertThat(longestExample, equalTo(""));
    }

    @Test
    public void getShortestExample_withFixedLengthRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{9}");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        String expected = repeat('a', 9);
        Assert.assertThat(shortestExample, equalTo(expected));
    }

    @Test
    public void getShortestExample_withRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,9}");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        Assert.assertThat(shortestExample, equalTo(""));
    }

    @Test
    public void getShortestExample_withLongRangeRegex_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,1000}");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        Assert.assertThat(shortestExample, equalTo(""));
    }

    @Test
    public void getShortestExample_withSimpleRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[f]{0,10}([a-c]{3}|[d-e]{5})");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        String expected = repeat('a', 3);
        Assert.assertThat(shortestExample, equalTo(expected));
    }

    @Test
    public void getShortestExample_withComplexRegexContainingOptionalPaths_shouldReturnCorrectString(){
        Automaton automaton = getAutomaton("[a-z]{0,10}([a-c]{3}|[d-e]{5})");

        String shortestExample = AutomatonUtils.getShortestExample(automaton);

        Assert.assertThat(shortestExample, matchesPattern("[a-z]{3}"));
    }

    @Test
    public void getShortestExample_withLengthLimitedEmailAddress_shouldReturnCorrectStringWithLowestOrdinalCharPerSection(){
        Automaton email = getAutomaton("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$");
        Automaton lengthRestriction = getAutomaton("^.{0,20}$");
        Automaton intersected = email.intersection(lengthRestriction);

        String longestExample = AutomatonUtils.getShortestExample(intersected);

        Assert.assertThat(longestExample.length(), equalTo(7));
        Assert.assertThat(longestExample, matchesPattern("^[a-z0-9]+\\@[a-z0-9]+\\.co(m|\\.uk)$"));
    }

    @Test
    public void getShortestExample_withEmptyString_shouldReturnEmptyString(){
        Automaton automaton = getAutomaton("[a-z]{0}");

        String longestExample = AutomatonUtils.getShortestExample(automaton);

        Assert.assertThat(longestExample, equalTo(""));
    }

    private static Automaton getAutomaton(String regex){
        return RegexStringGenerator.createAutomaton(regex, true, new HashMap<>());
    }

    private static String repeat(Character character, int times){
        return IntStream.range(0, times).mapToObj(index -> character.toString()).collect(Collectors.joining(""));
    }
}
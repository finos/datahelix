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

import dk.brics.automaton.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class AutomatonUtils {

    private static final Map<String, String> PREDEFINED_CHARACTER_CLASSES = instantiatePredefinedCharacterClasses();

    private static final Pattern PATTERN_REQUOTED = Pattern.compile("\\\\Q(.*?)\\\\E");

    private static final Pattern PATTERN_SPECIAL = Pattern.compile("[.^$*+?(){|\\[\\\\@]");

    private static final Pattern START_ANCHOR_MATCHER = Pattern.compile("^\\^");

    private static final Pattern END_ANCHOR_MATCHER = Pattern.compile("\\$$");

    private AutomatonUtils() {
        throw new UnsupportedOperationException("No instantiation of static class");
    }

    private static Map<String, String> instantiatePredefinedCharacterClasses() {
        Map<String, String> characterClasses = new HashMap<>();
        characterClasses.put("\\\\d", "[0-9]");
        characterClasses.put("\\\\D", "[^0-9]");
        characterClasses.put("\\\\s", "[ \t\n\f\r]");
        characterClasses.put("\\\\S", "[^ \t\n\f\r]");
        characterClasses.put("\\\\w", "[a-zA-Z_0-9]");
        characterClasses.put("\\\\W", "[^a-zA-Z_0-9]");
        return Collections.unmodifiableMap(characterClasses);
    }
    /**
     * Get the longest string possible given the regex (in the form of the Automaton)
     * There may be many optional sections within a regex which need to be inspected to calculate the longest possible
     * string. The automaton has done the hard work of turning the regex into a set of transitions and states.
     * see https://github.com/finos/datahelix/blob/master/docs/developer/algorithmsAndDataStructures/StringGeneration.md for more detail.
     *
     * This method is a recursive implementation that will test each starting point calculating the string and returning
     * the longest possible variant.
     *
     * TODO:
     * 1) turn this into a non-recursive algorithm
     * 2) improve performance and/or memory management (don't build strings as part of the process)
     *
     * @param automaton The automaton that represents the regex
     * @return The longest possible string for the given regex/automaton
     */
    static String getLongestExample(Automaton automaton) {
        List<Transition> transitions = automaton.getInitialState()
            .getSortedTransitions(true)
            .stream()
            .sorted(Comparator.comparing(Transition::getMin))
            .collect(Collectors.toList()); //ensure some form or determinism

        TransitionLongestExampleCache cache = new TransitionLongestExampleCache();

        String longest = "";
        for (Transition transition: transitions) {
            String transitionLongest = getLongestExample(transition, cache);
            if (transitionLongest.length() > longest.length()) {
                longest = transitionLongest;
            }
        }

        return longest;
    }

    /**
     * A cache of transitions to calculated strings, to save recalculating the string fragment for a transition multiple
     * times.
     */
    private static class TransitionLongestExampleCache {
        private final Map<Transition, String> examples = new HashMap<>();

        String getLongestExample(Transition transition){
            if (examples.containsKey(transition)){
                return examples.get(transition);
            }

            String example = AutomatonUtils.getLongestExample(transition, this);
            examples.put(transition, example);
            return example;
        }
    }

    /**
     * Recursive function for calculating the longest string that can be generated from the given regular automaton
     *
     * @param transition The transition to start at
     * @param cache A cache of previously resolved transitions
     * @return The longest string that can be generated from the given transition
     */
    private static String getLongestExample(Transition transition, TransitionLongestExampleCache cache){
        if (transition == null){
            throw new IllegalArgumentException("Transition cannot be null");
        }

        StringBuilder string = new StringBuilder();

        while (transition != null) { //need some exit condition
            State dest = transition.getDest();
            string.append((char)Math.max(printableChar, transition.getMin()));

            if (dest.isAccept() && dest.getTransitions().isEmpty()){
                return string.toString();
            }

            Map<State, List<Transition>> transitionsToUniqueStates = dest.getTransitions()
                .stream()
                .sorted(Comparator.comparing(Transition::getMin))
                .collect(Collectors.groupingBy(Transition::getDest));
            if (transitionsToUniqueStates.size() == 1){
                //every transition goes to the same destination, just use the first
                Transition nextTransition = transitionsToUniqueStates.values().iterator().next().get(0);

                if (nextTransition == transition){
                    //infinite generation
                    return string.toString();
                }

                transition = nextTransition;
                continue;
            }

            List<Transition> nextTransitions = transitionsToUniqueStates.values()
                .stream()
                .map(transitions -> transitions.get(0))
                .collect(Collectors.toList());

            //there are multiple suffix's, append the longest one
            String longestSuffix = "";
            for (Transition nextTransition: nextTransitions) {
                String suffix = cache.getLongestExample(nextTransition);
                if (suffix.length() > longestSuffix.length()){
                    longestSuffix = suffix;
                }
            }

            string.append(longestSuffix);
            return string.toString();
        }

        throw new IllegalStateException("Would only get here if transition is null, this should never happen");
    }

    // Taken from Automaton but updated to return printable characters
    static String getShortestExample(Automaton a) {
        State initialState = a.getInitialState();

        Map<State, String> stateToOutput = new HashMap<>();
        LinkedList<State> queue = new LinkedList<>();

        stateToOutput.put(initialState, "");
        queue.add(initialState);

        String currentBest = null;

        while (!queue.isEmpty()) {
            State currentState = queue.removeFirst();
            String currentOutput = stateToOutput.get(currentState);

            if (currentState.isAccept()) {

                if (currentBest == null
                    || currentOutput.length() < currentBest.length()
                    || (currentOutput.length() == currentBest.length() && currentOutput.compareTo(currentBest) < 0)) {
                    currentBest = currentOutput;
                }

            } else {
                for (Transition transition : currentState.getTransitions()) {
                    String nextOutput = stateToOutput.get(transition.getDest());
                    String nextOutputCalculated = currentOutput + (char)Math.max(transition.getMin(), printableChar);

                    if (nextOutput == null
                        || (nextOutput.length() == nextOutputCalculated.length() && nextOutputCalculated.compareTo(nextOutput) < 0)) {
                        if (nextOutput == null) {
                            queue.addLast(transition.getDest());
                        }
                        stateToOutput.put(transition.getDest(), nextOutputCalculated);
                    }
                }
            }
        }

        return currentBest;
    }

    /**
     * Create an automaton and store its instance in the cache, keyed on the given regex
     * The cache will vary based on &lt;matchFullString&gt;.
     * <p>
     * The creation of an automaton is a time-consuming process, especially for more complex expressions.
     *
     * @param regexStr        The string to create the automaton from
     * @param matchFullString Whether the string represents a matchingRegex (true) or containingRegex (false) expression
     * @param cache           The cache to store the automaton instance in
     * @return The created automaton
     */
    public static Automaton createAutomaton(String regexStr, boolean matchFullString, Map<String, Automaton> cache) {
        final String anchoredStr = convertEndAnchors(regexStr, matchFullString);
        final String requotedStr = escapeCharacters(anchoredStr);
        final RegExp bricsRegExp = expandShorthandClasses(requotedStr);

        Automaton generatedAutomaton = bricsRegExp.toAutomaton();
        generatedAutomaton.expandSingleton();
        // NB: AF if want to allow cmd line option to expand to a fuller character set make sure don't
        // make it unbounded as we don't want to see tabs or back spaces or null (\u0000) unicode chars
        generatedAutomaton = restrictCharacterSet(generatedAutomaton, '\u0020', '\u007E');

        cache.put(regexStr, generatedAutomaton);
        return generatedAutomaton;
    }

    private static Automaton restrictCharacterSet(Automaton generatedAutomaton, char minChar, char maxChar) {
        return BasicOperations.intersection(
            Automaton.makeCharRange(minChar, maxChar).repeat(),
            generatedAutomaton);
    }

    private static String escapeCharacters(String regex) {
        StringBuilder sb = new StringBuilder(regex);
        Matcher matcher = PATTERN_REQUOTED.matcher(sb);
        while (matcher.find()) {
            sb.replace(matcher.start(), matcher.end(), PATTERN_SPECIAL.matcher(matcher.group(1)).replaceAll("\\\\$0"));
        }
        return sb.toString();
    }

    private static String convertEndAnchors(String regexStr, boolean matchFullString) {
        final Matcher startAnchorMatcher = START_ANCHOR_MATCHER.matcher(regexStr);

        if (startAnchorMatcher.find()) {
            // brics.RegExp doesn't use anchors - they're treated as literal ^/$ characters
            regexStr = startAnchorMatcher.replaceAll("");
        } else if (!matchFullString) {
            // brics.RegExp only supports full string matching, so add .* to simulate it
            regexStr = ".*" + regexStr;
        }

        final Matcher endAnchorMatcher = END_ANCHOR_MATCHER.matcher(regexStr);

        if (endAnchorMatcher.find()) {
            regexStr = endAnchorMatcher.replaceAll("");
        } else if (!matchFullString) {
            regexStr = regexStr + ".*";
        }

        return regexStr;
    }

    /*
     * As the Briks regex parser doesn't recognise shorthand classes we need to convert them to character groups
     */
    private static RegExp expandShorthandClasses(String regex) {
        String finalRegex = regex;
        for (Map.Entry<String, String> charClass : PREDEFINED_CHARACTER_CLASSES.entrySet()) {
            finalRegex = finalRegex.replaceAll(charClass.getKey(), charClass.getValue());
        }
        return new RegExp(finalRegex);
    }

    private static final char printableChar = ' ';


}

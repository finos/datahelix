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

public class AutomatonUtils {

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

        // The automaton is determinised to improve performance. See
        // https://en.wikipedia.org/wiki/Nondeterministic_finite_automaton#Equivalence_to_DFA
        // for details.
        generatedAutomaton.determinize();
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
}

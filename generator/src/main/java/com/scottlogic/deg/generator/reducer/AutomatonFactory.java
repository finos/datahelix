package com.scottlogic.deg.generator.reducer;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AutomatonFactory {
    private static final Map<String, String> PREDEFINED_CHARACTER_CLASSES;
    static {
        Map<String, String> characterClasses = new HashMap<>();
        characterClasses.put("\\\\d", "[0-9]");
        characterClasses.put("\\\\D", "[^0-9]");
        characterClasses.put("\\\\s", "[ \t\n\f\r]");
        characterClasses.put("\\\\S", "[^ \t\n\f\r]");
        characterClasses.put("\\\\w", "[a-zA-Z_0-9]");
        characterClasses.put("\\\\W", "[^a-zA-Z_0-9]");
        PREDEFINED_CHARACTER_CLASSES = Collections.unmodifiableMap(characterClasses);
    }

    public Automaton fromPattern(Pattern pattern) {
        final String regexStr = pattern.pattern();
        final String requotedStr = requote(regexStr);
        final RegExp bricsRegExp = createRegExp(requotedStr);
        return bricsRegExp.toAutomaton();
    }

    /**
     * http://stackoverflow.com/questions/399078/what-special-characters-must-be-escaped-in-regular-expressions
     * adding "@" prevents StackOverflowError: https://github.com/mifmif/Generex/issues/21
     * @param regex
     * @return
     */
    private String requote(String regex) {
        final Pattern patternRequoted = Pattern.compile("\\\\Q(.*?)\\\\E");
        final Pattern patternSpecial = Pattern.compile("[.^$*+?(){|\\[\\\\@]");
        StringBuilder sb = new StringBuilder(regex);
        Matcher matcher = patternRequoted.matcher(sb);
        while (matcher.find()) {
            sb.replace(matcher.start(), matcher.end(), patternSpecial.matcher(matcher.group(1)).replaceAll("\\\\$0"));
        }
        return sb.toString();
    }

    private RegExp createRegExp(String regex) {
        String finalRegex = regex;
        for (Map.Entry<String, String> charClass : PREDEFINED_CHARACTER_CLASSES.entrySet()) {
            finalRegex = finalRegex.replaceAll(charClass.getKey(), charClass.getValue());
        }
        return new RegExp(finalRegex);
    }

}

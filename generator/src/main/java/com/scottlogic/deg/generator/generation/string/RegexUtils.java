package com.scottlogic.deg.generator.generation.string;

import dk.brics.automaton.RegExp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils {

    private static final Map<String, String> PREDEFINED_CHARACTER_CLASSES = instantiatePredefinedCharacterClasses();

    private static final Pattern PATTERN_REQUOTED = Pattern.compile("\\\\Q(.*?)\\\\E");

    private static final Pattern PATTERN_SPECIAL = Pattern.compile("[.^$*+?(){|\\[\\\\@]");

    private static final Pattern START_ANCHOR_MATCHER = Pattern.compile("^\\^");

    private static final Pattern END_ANCHOR_MATCHER = Pattern.compile("\\$$");

    private RegexUtils() {
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

    public static String escapeCharacters(String regex) {
        StringBuilder sb = new StringBuilder(regex);
        Matcher matcher = PATTERN_REQUOTED.matcher(sb);
        while (matcher.find()) {
            sb.replace(matcher.start(), matcher.end(), PATTERN_SPECIAL.matcher(matcher.group(1)).replaceAll("\\\\$0"));
        }
        return sb.toString();
    }

    public static String convertEndAnchors(String regexStr, boolean matchFullString) {
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
    public static RegExp expandShorthandClasses(String regex) {
        String finalRegex = regex;
        for (Map.Entry<String, String> charClass : PREDEFINED_CHARACTER_CLASSES.entrySet()) {
            finalRegex = finalRegex.replaceAll(charClass.getKey(), charClass.getValue());
        }
        return new RegExp(finalRegex);
    }

}

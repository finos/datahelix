package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.generation.NoStringsStringGenerator;
import com.scottlogic.deg.generator.generation.RegexStringGenerator;
import com.scottlogic.deg.generator.generation.StringGenerator;
import com.scottlogic.deg.generator.utils.SetUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TextualRestrictions implements StringRestrictions {
    private static final Integer defaultMinLength = 0;
    private static final Integer defaultMaxLength = 255;

    private final Integer minLength;
    private final Integer maxLength;
    private final Set<Integer> excludedLengths;
    private final Set<Pattern> matchingRegex;
    private final Set<Pattern> containingRegex;
    private final Set<Pattern> notMatchingRegex;
    private final Set<Pattern> notContainingRegex;
    private StringGenerator generator;

    private TextualRestrictions(
        Integer minLength,
        Integer maxLength,
        Set<Pattern> matchingRegex,
        Set<Pattern> containingRegex,
        Set<Integer> excludedLengths,
        Set<Pattern> notMatchingRegex,
        Set<Pattern> notContainingRegex) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.matchingRegex = matchingRegex;
        this.containingRegex = containingRegex;
        this.excludedLengths = excludedLengths;
        this.notMatchingRegex = notMatchingRegex;
        this.notContainingRegex = notContainingRegex;
    }

    public static TextualRestrictions withStringMatching(Pattern pattern, boolean negate) {
        return new TextualRestrictions(
            defaultMinLength,
            defaultMaxLength,
            negate
                ? Collections.emptySet()
                : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(pattern)
                : Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public static TextualRestrictions withStringContaining(Pattern pattern, boolean negate) {
        return new TextualRestrictions(
            defaultMinLength,
            defaultMaxLength,
            Collections.emptySet(),
            negate
                ? Collections.emptySet()
                : Collections.singleton(pattern),
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(pattern)
                : Collections.emptySet()
        );
    }

    public static TextualRestrictions withLength(int length, boolean negate) {
        return new TextualRestrictions(
            negate ? defaultMinLength : length,
            negate ? defaultMaxLength : length,
            Collections.emptySet(),
            Collections.emptySet(),
            negate
                ? Collections.singleton(length)
                : Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public static TextualRestrictions withMinLength(int length){
        return new TextualRestrictions(
            length,
            defaultMaxLength,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    public static TextualRestrictions withMaxLength(int length){
        return new TextualRestrictions(
            defaultMinLength,
            length,
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet(),
            Collections.emptySet()
        );
    }

    @Override
    public boolean match(String x) {
        return createGenerator().match(x);
    }

    /**
     * Produce another StringRestrictions object that represents the constraints from @param other as well as those in
     * this instance.
     *
     * e.g. &lt;shorterThan 10&gt; intersect &lt;longerThan 5&gt; &rarr; &lt;shorterThan 10 &amp; longerThan 5&gt;
     */
    public StringRestrictions intersect(StringRestrictions other){
        if (other instanceof NoStringsPossibleStringRestrictions){
            return other;
        }

        if (other instanceof MatchesStandardStringRestrictions){
            return new NoStringsPossibleStringRestrictions("Cannot merge textual constraints with aValid constraints");
        }

        TextualRestrictions textualRestrictions = (TextualRestrictions) other;

        return new TextualRestrictions(
            mergeMinLengths(textualRestrictions.minLength),
            mergeMaxLengths(textualRestrictions.maxLength),
            SetUtils.union(matchingRegex, textualRestrictions.matchingRegex),
            SetUtils.union(containingRegex, textualRestrictions.containingRegex),
            SetUtils.union(excludedLengths, textualRestrictions.excludedLengths),
            SetUtils.union(notMatchingRegex, textualRestrictions.notMatchingRegex),
            SetUtils.union(notContainingRegex, textualRestrictions.notContainingRegex)
        );
    }

    @Override
    public boolean isContradictory() {
        if (matchingRegex.isEmpty() && containingRegex.isEmpty()){
            return false; //no regular expressions exist that can contradict
        }

        StringGenerator generator = createGenerator();
        return generator instanceof NoStringsStringGenerator;
    }

    private Integer mergeMinLengths(Integer otherMinLength) {
        if (minLength == null){
            return otherMinLength;
        }

        return Math.max(minLength, otherMinLength);
    }

    private Integer mergeMaxLengths(Integer otherMaxLength) {
        if (maxLength == null){
            return otherMaxLength;
        }

        return Math.min(maxLength, otherMaxLength);
    }

    public boolean match(Object o) {
        if (!StringRestrictions.isString(o)) {
            return false;
        }

        String s = (String) o;
        return createGenerator().match(s);
    }

    /**
     * Singleton method: Will always return the same instance after the first successful execution on this instance
     *
     * Create a StringGenerator that will produce strings that match all of the given constraints
     */
    public StringGenerator createGenerator() {
        if (generator != null){
            return generator;
        }

        //determine the boundaries and exclusions defined in the given constraints
        int minLength = this.minLength != null ? this.minLength : 0;
        int maxLength = this.maxLength != null ? this.maxLength : StringRestrictions.MAX_STRING_LENGTH;

        //detect contradictions
        if (minLength > maxLength
            || allLengthsAreExcluded(minLength, maxLength, excludedLengths)) {
            return generator = new NoStringsStringGenerator("Lengths are contradictory");
        }

        //produce a regex, and a generator for it, that can produce ANY string within the given bounds
        //emits /.{&lt;shortest&gt;,&lt;longest&gt;}/
        //can also emit /.{&lt;0&gt;,&lt;5&gt;}|.{&lt;7&gt;,&lt;255&gt;}/ if 6 is an excluded length
        StringGenerator lengthConstrainingGenerator = new RegexStringGenerator(
            createStringLengthRestrictionRegex(minLength, maxLength),
            true);

        //combine (merge/intersect) each non-length related constraint to produce a single string generator
        //e.g. would combine /[a-z]{0,9}/ with /.{0,255}/ (lengthConstrainingGenerator) to produce a single generator
        //that looks like /[a-z]{0,9} ∩ .{0,255}/, which is equivalent to /[a-z]{0,9}/
        return generator = getPatternConstraints()
            .reduce(
                lengthConstrainingGenerator,
                (prev, current) -> {
                    if (prev instanceof NoStringsStringGenerator){
                        return prev;
                    }

                    return prev.intersect(current);
                },
                (a, b) -> null);
    }

    private boolean allLengthsAreExcluded(int minLength, int maxLength, Set<Integer> excludedLengths) {
        long permittedLengths = IntStream.range(minLength, maxLength + 1)
            .filter(length -> !excludedLengths.contains(length))
            .count();

        return permittedLengths == 0;
    }

    private Stream<StringGenerator> getPatternConstraints() {
        return concatStreams(
            getStringGenerators(matchingRegex, regex -> new RegexStringGenerator(regex, true)),
            getStringGenerators(containingRegex, regex -> new RegexStringGenerator(regex, false)),
            getStringGenerators(notMatchingRegex, regex -> new RegexStringGenerator(regex, true).complement()),
            getStringGenerators(notContainingRegex, regex -> new RegexStringGenerator(regex, false).complement())
        );
    }

    @SafeVarargs
    private static <T> Stream<T> concatStreams(Stream<T>... streams){
        return Arrays
            .stream(streams)
            .reduce(Stream::concat)
            .orElse(Stream.empty());
    }

    private static Stream<StringGenerator> getStringGenerators(Set<Pattern> patterns, Function<String, StringGenerator> getGenerator) {
        if (patterns.isEmpty()){
            return Stream.empty();
        }

        return patterns.stream().map(p -> getGenerator.apply(p.toString()));
    }

    /**
     * Produce a regular expression that permits any character, but restricts the length of the generated string
     * Will either:
     * 1. Return a regex like /.{nnn}/ where nnn is the defined length constraint, or the shorterThan and longerThan are the same
     * 2. Return a regex like /.{aaa,bbb}/ where aaa is the shortest length and bbb is the longest length
     * 3. Return a regex like /.{0,aaa}|.{bbb,ccc}/ where
     *      aaa is the last length before an excluded length
     *      bbb is the first length after the previously excluded length
     *      ccc is the appropriate maximum length for the string
     *
     * The appropriate maximum length is either 255 or 1000, depending on whether the string length constraints breach
     * the 255 boundary. If they do, 1000 will be used, otherwise 255 (the default) will be used.
     *
     * @param minLength the minimum length for the string
     * @param maxLength the maximum length for the string
     */
    private String createStringLengthRestrictionRegex(int minLength, int maxLength) {
        if (minLength == maxLength){
            //longerThan 5 & shorterThan 7, only possible string is 6 (5 + 1)
            return restrictStringLength(minLength);
        }

        //if there are no excluded lengths, then return a regex that represents the shortest/longest string
        if (excludedLengths.isEmpty()) {
            return restrictStringLength(minLength, maxLength);
        }

        List<Integer> orderedExcludedLengths = excludedLengths.stream().sorted().collect(Collectors.toList()); //ensure the excluded lengths are ordered
        List<String> regexes = new ArrayList<>();
        Integer lastExcludedLength = null;
        for (int excludedLength : orderedExcludedLengths) {
            if (excludedLength > maxLength || excludedLength < minLength){
                continue; //the excluded length is beyond the permitted length, ignore it
            }
            if (excludedLength == maxLength){
                maxLength--; //the excluded length is the same as the longest, reduce the max-length
            }
            if (excludedLength == minLength){
                lastExcludedLength = excludedLength;
                continue;
            }

            if (lastExcludedLength == null) {
                if (minLength < excludedLength - 1) {
                    regexes.add(String.format(".{%d,%d}", minLength, excludedLength - 1));
                }
            } else {
                regexes.add(String.format(".{%d,%d}", lastExcludedLength + 1, excludedLength - 1));
            }

            lastExcludedLength = excludedLength;
        }

        if (regexes.isEmpty()) {
            //if no lengths have been excluded, i.e. each excluded length is either > maxLength or
            // the same maxLength -1 (at which point appropriateMaxLength will have been modified)
            return restrictStringLength(lastExcludedLength != null ? lastExcludedLength + 1 : minLength, maxLength);
        }

        if (lastExcludedLength + 1 < maxLength - 1) {
            regexes.add(String.format(".{%d,%d}", lastExcludedLength + 1, maxLength));
        }

        return String.format(
            regexes.size() == 1 ? "^%s$" : "^(%s)$",
            String.join("|", regexes));
    }

    private String restrictStringLength(int length){
        return String.format("^.{%d}$", length);
    }

    private String restrictStringLength(int min, int max){
        return String.format("^.{%d,%d}$", min, max);
    }

    @Override
    public String toString() {
        return String.format("Strings: %d..%d (not: %s)\nmatching: %s\ncontaining: %s\nnotMatching: %s\nnotContaining: %s",
            minLength != null ? minLength : 0,
            maxLength != null ? maxLength : Integer.MAX_VALUE,
            excludedLengths.toString(),
            patternsAsString(matchingRegex),
            patternsAsString(containingRegex),
            patternsAsString(notMatchingRegex),
            patternsAsString(notContainingRegex));
    }

    private String patternsAsString(Set<Pattern> patterns) {
        return patterns
            .stream()
            .map(Pattern::toString)
            .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextualRestrictions that = (TextualRestrictions) o;

        return excludedLengths.equals(that.excludedLengths)
            && maxLength.equals(that.maxLength)
            && ((minLength == null && that.minLength == null) || (minLength != null && minLength.equals(that.minLength)))
            && containingRegex.equals(that.containingRegex)
            && matchingRegex.equals(that.matchingRegex)
            && notContainingRegex.equals(that.notContainingRegex)
            && notMatchingRegex.equals(that.notMatchingRegex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(excludedLengths, maxLength, minLength, containingRegex, matchingRegex, notMatchingRegex, notContainingRegex);
    }
}

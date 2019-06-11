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
    final Integer minLength;
    final Integer maxLength;
    final Set<Integer> excludedLengths;
    final Set<Pattern> matchingRegex;
    final Set<Pattern> containingRegex;
    final Set<Pattern> notMatchingRegex;
    final Set<Pattern> notContainingRegex;
    private StringGenerator generator;

    TextualRestrictions(
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

    @Override
    public boolean match(String x) {
        return createGenerator().match(x);
    }

    /**
     * Produce a new string restrictions instance that represents the intersection of this and the other given restrictions
     * See MatchesStandardStringRestrictions.intersect() for more details on how the `aValid` operator can be merged
     */
    public MergeResult<StringRestrictions> intersect(StringRestrictions other){
        if (other == null){
            throw new IllegalArgumentException("Other StringRestrictions must not be null");
        }

        if (other instanceof NoStringsPossibleStringRestrictions){
            return new MergeResult<>(other);
        }

        if (other instanceof MatchesStandardStringRestrictions){
            return other.intersect(this);
        }

        if (!(other instanceof TextualRestrictions)){
            throw new UnsupportedOperationException("Unable to produce intersection of TextualRestrictions and " + other.getClass().getName());
        }

        TextualRestrictions textualRestrictions = (TextualRestrictions) other;

        TextualRestrictions merged = new TextualRestrictions(
            mergeMinLengths(textualRestrictions.minLength),
            mergeMaxLengths(textualRestrictions.maxLength),
            SetUtils.union(matchingRegex, textualRestrictions.matchingRegex),
            SetUtils.union(containingRegex, textualRestrictions.containingRegex),
            SetUtils.union(excludedLengths, textualRestrictions.excludedLengths),
            SetUtils.union(notMatchingRegex, textualRestrictions.notMatchingRegex),
            SetUtils.union(notContainingRegex, textualRestrictions.notContainingRegex)
        );

        return merged.isContradictory()
            ? MergeResult.unsuccessful()
            : new MergeResult<>(merged);
    }

    /**
     * Detect if the intersection of length constraints & any given regex constraints contradict (can't emit any values)
     *
     * @return Whether this restrictions type is contradictory
     */
    private boolean isContradictory() {
        if (matchingRegex.isEmpty() && containingRegex.isEmpty()){
            return false; //no regular expressions exist that can contradict
        }

        StringGenerator generator = createGenerator();
        return generator instanceof NoStringsStringGenerator;
    }

    /**
     * Yield the appropriate minimum length from self and/or other min length
     *
     * @param otherMinLength Optionally another length to compare
     * @return The appropriate or highest of the lengths
     */
    private Integer mergeMinLengths(Integer otherMinLength) {
        if (minLength == null){
            return otherMinLength;
        }
        if (otherMinLength == null){
            return minLength;
        }

        return Math.max(minLength, otherMinLength);
    }

    /**
     * Yield the appropriate maximum length from self and/or other max length
     *
     * @param otherMaxLength Optionally another length to compare
     * @return The appropriate or lowest of the lengths
     */
    private Integer mergeMaxLengths(Integer otherMaxLength) {
        if (maxLength == null){
            return otherMaxLength;
        }
        if (otherMaxLength == null){
            return maxLength;
        }

        return Math.min(maxLength, otherMaxLength);
    }

    public boolean match(Object o) {
        if (!isInstanceOf(o)) {
            return false;
        }

        String s = (String) o;
        StringGenerator generator = createGenerator();
        return generator == null || generator.match(s);
    }

    /**
     * Singleton method: Will always return the same instance after the first successful execution on this instance
     *
     * Create a StringGenerator that will produce strings that match all of the given constraints
     * Yield NoStringsGenerator if no strings could be produced for the given restrictions
     */
    public StringGenerator createGenerator() {
        if (generator != null){
            return generator;
        }

        int minLength = this.minLength != null ? this.minLength : 0;

        //detect contradictions
        if (maxLength != null && (minLength > maxLength
            || allLengthsAreExcluded(minLength, maxLength, excludedLengths))) {
            return generator = new NoStringsStringGenerator("Lengths are contradictory");
        }

        //produce a regex, and a generator for it, that can produce ANY string within the given bounds
        //emits /.{&lt;shortest&gt;,&lt;longest&gt;}/
        //can also emit /.{&lt;0&gt;,&lt;5&gt;}|.{&lt;7&gt;,&lt;255&gt;}/ if 6 is an excluded length
        StringGenerator lengthConstrainingGenerator = minLength == 0 && maxLength == null && excludedLengths.isEmpty()
            ? null
            : new RegexStringGenerator(
                createStringLengthRestrictionRegex(minLength, maxLength),
                true);

        //combine (merge/intersect) each non-length related constraint to produce a single string generator
        //e.g. would combine /[a-z]{0,9}/ with /.{0,255}/ (lengthConstrainingGenerator) to produce a single generator
        //that looks like /[a-z]{0,9} ∩ .{0,255}/, which is equivalent to /[a-z]{0,9}/
        return generator = getPatternConstraints()
            .reduce(
                lengthConstrainingGenerator,
                (prev, current) -> {
                    if (prev == null){
                        return current;
                    }

                    if (prev instanceof NoStringsStringGenerator){
                        return prev;
                    }

                    return prev.intersect(current);
                },
                (a, b) -> null);
    }

    /**
     * Detect if the list of excluded lengths represents all possible string-lengths that could be produced
     *
     * @param minLength The minimum string length
     * @param maxLength The maximum string length if one is defined, will always return false if this is null
     * @param excludedLengths The set of excluded lengths
     * @return Whether excludedLengths represents all possible string lengths, and therefore no strings could be created
     */
    private static boolean allLengthsAreExcluded(int minLength, Integer maxLength, Set<Integer> excludedLengths) {
        if (maxLength == null){
            return false; //cannot determine for unbounded lengths
        }

        long permittedLengths = IntStream.range(minLength, maxLength + 1)
            .filter(length -> !excludedLengths.contains(length))
            .count();

        return permittedLengths == 0;
    }

    /**
     * Get a stream of StringGenerators that represent each regex restriction in this type
     */
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
     * 1. Return a regex like /.{nnn}/ where nnn is the defined length constraint, or the shorterThan and longerThan represent the same string length
     * 2. Return a regex like /.{aaa,bbb}/ where aaa is the shortest length and bbb is the longest length
     * 3. Return a regex like /.{0,aaa}|.{bbb,ccc}/ where
     *      aaa is the last length before an excluded length
     *      bbb is the first length after the previously excluded length
     *      ccc is the appropriate maximum length for the string
     *
     * @param minLength the minimum length for the string
     * @param maxLength the maximum length for the string
     */
    private String createStringLengthRestrictionRegex(int minLength, Integer maxLength) {
        if (maxLength != null && maxLength.equals(minLength)){
            return restrictStringLength(minLength);
        }

        //if there are no excluded lengths, then return a regex that represents the shortest/longest string
        if (excludedLengths.isEmpty()) {
            return restrictStringLength(minLength, maxLength);
        }

        List<Integer> orderedExcludedLengths = excludedLengths.stream().sorted().collect(Collectors.toList()); //ensure the excluded lengths are ordered
        List<String> patterns = new ArrayList<>();
        Integer lastExcludedLength = null;
        for (int excludedLength : orderedExcludedLengths) {
            if ((maxLength != null && excludedLength > maxLength) || excludedLength < minLength){
                continue; //the excluded length is beyond the permitted length, ignore it
            }
            if (maxLength != null && excludedLength == maxLength){
                maxLength--; //the excluded length is the same as the longest, reduce the max-length
            }
            if (excludedLength == minLength){
                lastExcludedLength = excludedLength;
                continue;
            }

            if (lastExcludedLength == null) {
                if (minLength < excludedLength - 1) {
                    patterns.add(String.format(".{%d,%d}", minLength, excludedLength - 1));
                }
            } else {
                patterns.add(String.format(".{%d,%d}", lastExcludedLength + 1, excludedLength - 1));
            }

            lastExcludedLength = excludedLength;
        }

        if (patterns.isEmpty()) {
            //if no lengths have been excluded, i.e. each excluded length is either > maxLength or
            // the same maxLength -1 (at which point appropriateMaxLength will have been modified)
            return restrictStringLength(lastExcludedLength != null ? lastExcludedLength + 1 : minLength, maxLength);
        }

        if (maxLength != null && lastExcludedLength + 1 < maxLength - 1) {
            patterns.add(String.format(".{%d,%d}", lastExcludedLength + 1, maxLength));
        } else if (maxLength == null){
            patterns.add(String.format(".{%d,}", lastExcludedLength + 1));
        }

        return String.format(
            patterns.size() == 1 ? "^%s$" : "^(%s)$",
            String.join("|", patterns));
    }

    private String restrictStringLength(int length){
        return String.format("^.{%d}$", length);
    }

    private String restrictStringLength(int min, Integer max){
        if (max == null) {
            return String.format("^.{%d,}$", min);
        }

        return String.format("^.{%d,%d}$", min, max);
    }

    @Override
    public String toString() {
        return String.format("Strings: %d..%s%s%s%s%s%s",
            minLength != null ? minLength : 0,
            maxLength != null ? maxLength.toString() : "",
            excludedLengths.isEmpty() ? "" : " not lengths " + excludedLengths,
            matchingRegex.isEmpty() ? "" : " matching: " + patternsAsString(matchingRegex),
            containingRegex.isEmpty() ? "" : " containing: " + patternsAsString(containingRegex),
            notMatchingRegex.isEmpty() ? "" : " not matching: " + patternsAsString(notMatchingRegex),
            notContainingRegex.isEmpty() ? "" : " not containing: " + patternsAsString(notContainingRegex));
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
            && ((maxLength == null && that.maxLength == null) || (maxLength != null && maxLength.equals(that.maxLength)))
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

package com.scottlogic.deg.generator.restrictions;

import com.scottlogic.deg.generator.constraints.atomic.*;
import com.scottlogic.deg.generator.generation.*;
import com.scottlogic.deg.generator.walker.reductive.Merged;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * https://github.com/ScottLogic/datahelix/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/util/RegexProcessor.java
 * https://github.com/ScottLogic/datahelix/blob/ws/experimental-data-constraint-solver/data-constraint-poc/src/main/java/com/scottlogic/deg/constrainer/RegexFieldConstraint.java#L133
 */
public class StringRestrictions {
    private static final int DEFAULT_SHORTER_THAN = 255 + 1;
    private static final int MAX_SHORTER_THAN = 1000 + 1;

    final Set<AtomicConstraintState> constraints;
    private StringGenerator generator;

    private class AtomicConstraintState {
        private final AtomicConstraint constraint;
        private final boolean negated;

        AtomicConstraintState(AtomicConstraint constraint, boolean negated) {
            if (constraint instanceof NotConstraint) {
                throw new IllegalStateException("NotConstraint should not exist here");
            }

            this.constraint = constraint;
            this.negated = negated;
        }

        public boolean equals(Object o){
            if (!(o instanceof AtomicConstraintState)){
                return false;
            }

            AtomicConstraintState other = (AtomicConstraintState) o;
            return other.negated == negated
                && other.constraint.equals(constraint);
        }

        public String toString(){
            return negated
                ? String.format("¬(%s)", constraint)
                : constraint.toString();
        }
    }

    public StringRestrictions(AtomicConstraint constraint, boolean negate) {
        this.constraints = Collections.singleton(new AtomicConstraintState(constraint, negate));
    }

    private StringRestrictions(Set<AtomicConstraintState> constraints){
        this.constraints = constraints;
    }

    /**
     * Produce another StringRestrictions object that represents the constraints from @param other as well as those in
     * this instance.
     *
     * e.g. &lt;shorterThan 10&gt; intersect &lt;longerThan 5&gt; &rarr; &lt;shorterThan 10 &amp; longerThan 5&gt;
     *
     * @param other
     * @return
     */
    StringRestrictions intersect(StringRestrictions other){
        return new StringRestrictions(
            Stream.concat(
                constraints.stream(),
                other.constraints.stream())
            .collect(Collectors.toSet())
        );
    }

    public static boolean isString(Object o) {
        return o instanceof String;
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
     * @return
     */
    public StringGenerator createGenerator() {
        if (generator != null){
            return generator;
        }

        //if there are any MatchesStandardConstraints then return a generator for it
        //if there are some non-MatchesStandardConstraints, then ensure no strings are created - not currently supported
        if (constraints.stream().anyMatch(cs -> cs.constraint instanceof MatchesStandardConstraint)) {
            return generator = createMatchesStandardGenerator();
        }

        //determine the boundaries and exclusions defined in the given constraints
        Merged<Integer> longerThan = getLongerThan();
        Merged<Integer> shorterThan = getShorterThan();
        Merged<Integer> ofLength = getOfLength();
        Set<Integer> excludedLengths = getExcludedLengths();

        //detect contradictions
        if (longerThan.isContradictory()
            || shorterThan.isContradictory()
            || (ofLength != null && ofLength.isContradictory())
            || longerThan.get() > shorterThan.get()  //e.g. longerThan 5 and shorterThan 2
            || (ofLength != null && ofLength.get() >= shorterThan.get()) //e.g ofLength 5 and shorterThan 2
            || (ofLength != null && ofLength.get() <= longerThan.get()) //e.g. ofLength 5 and longerThan 10
            || (ofLength != null && excludedLengths.contains(ofLength.get()))){ //e.g. ofLength 5 and not(ofLength 5)
            //check for contradictions
            return generator = new NoStringsStringGenerator("Lengths are contradictory");
        }

        //produce a regex, and a generator for it, that can produce ANY string within the given bounds
        //emits /.{&lt;shortest&gt;,&lt;longest&gt;}/
        //can also emit /.{&lt;0&gt;,&lt;5&gt;}|.{&lt;7&gt;,&lt;255&gt;}/ if 6 is an excluded length
        StringGenerator lengthConstrainingGenerator = new RegexStringGenerator(
            createStringLengthRestrictionRegex(longerThan.get() + 1, shorterThan.get() - 1, ofLength != null ? ofLength.get() : null, excludedLengths),
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

                    StringGenerator generator = createGenerator(current.constraint);

                    return prev.intersect(current.negated
                        ? generator.complement()
                        : generator);
                },
                (a, b) -> null);
    }

    /**
     * Produce a regular expression that permits any character, but restricts the length of the generated string
     * Will either:
     * 1. Return a regex like /.{nnn}/ where nnn is the defined length constraint, or the shorterThan and longerThan are the same ??TODO: Fix this
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
     * @param ofLength the length the string must be equal to
     * @param excludedLengths the length the string cannot be equal to
     * @return
     */
    private String createStringLengthRestrictionRegex(int minLength, int maxLength, Integer ofLength, Set<Integer> excludedLengths) {
        if (ofLength != null){
            return restrictStringLength(ofLength);
        }

        if (minLength == maxLength){
            //longerThan 5 & shorterThan 7, only possible string is 6 (5 + 1)
            return restrictStringLength(minLength);
        }

        //if shorterThan or longerThan > 255 then use 1000, otherwise use 255.
        int appropriateMaxLength = Math.max(minLength, maxLength) > DEFAULT_SHORTER_THAN - 1
            ? Math.min(maxLength, MAX_SHORTER_THAN - 1)
            : Math.min(maxLength, DEFAULT_SHORTER_THAN - 1);

        //if there are no excluded lengths, then return a regex that represents the shortest/longest string
        if (excludedLengths.isEmpty()) {
            return restrictStringLength(minLength, appropriateMaxLength);
        }

        List<Integer> orderedExcludedLengths = excludedLengths.stream().sorted().collect(Collectors.toList()); //ensure the excluded lengths are ordered
        List<String> regexes = new ArrayList<>();
        Integer lastExcludedLength = null;
        for (int excludedLength : orderedExcludedLengths) {
            if (excludedLength > appropriateMaxLength || excludedLength < minLength){
                continue; //the excluded length is beyond the permitted length, ignore it
            }
            if (excludedLength == appropriateMaxLength){
                appropriateMaxLength--; //the excluded length is the same as the longest, reduce the max-length
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
            return restrictStringLength(lastExcludedLength != null ? lastExcludedLength + 1 : minLength, appropriateMaxLength);
        }

        if (lastExcludedLength + 1 < appropriateMaxLength - 1) {
            regexes.add(String.format(".{%d,%d}", lastExcludedLength + 1, appropriateMaxLength));
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

    private StringGenerator createMatchesStandardGenerator() {
        if (!constraints.stream().allMatch(cs -> cs.constraint instanceof MatchesStandardConstraint)) {
            return generator = new NoStringsStringGenerator("Cannot combine aValid and a textual constraint, see #487");
        }

        if (constraints.size() > 1){
            Set<StandardConstraintTypes> uniqueConstraintTypes = constraints.stream()
                .map(cs -> ((MatchesStandardConstraint) cs.constraint).standard)
                .collect(Collectors.toSet());

            if (uniqueConstraintTypes.size() > 1) {
                throw new UnsupportedOperationException("Cannot combine multiple aValid constraints");
            }

            if (constraints.stream().map(cs -> cs.negated).collect(Collectors.toSet()).size() == 2){
                //constraints are for the same standard and contain 2 unique negated values, therefore the constraints contradict
                //e.g. aValid isin & not(aValid isin)

                return new NoStringsStringGenerator("aValid constraints contradict with each other");
            }
        }

        AtomicConstraintState singleConstraint = constraints.iterator().next();
        StandardConstraintTypes standard = ((MatchesStandardConstraint)singleConstraint.constraint).standard;
        StringGenerator generator = getStringGenerator(standard);

        return singleConstraint.negated
            ? generator.complement()
            : generator;
    }

    private StringGenerator getStringGenerator(StandardConstraintTypes standard) {
        switch (standard){
            case ISIN:
                return new IsinStringGenerator();
            case SEDOL:
                return new SedolStringGenerator();
        }

        throw new UnsupportedOperationException("Unable to create string generator for: " + standard);
    }

    /**
     * @param constraint a non-length related and non-matches constraint
     * @return A string generator that can create strings for the given constraint
     */
    private StringGenerator createGenerator(AtomicConstraint constraint) {
        if (constraint instanceof ContainsRegexConstraint){
            return new RegexStringGenerator(((ContainsRegexConstraint) constraint).regex.toString(), false);
        }

        if (constraint instanceof MatchesRegexConstraint) {
            return new RegexStringGenerator(((MatchesRegexConstraint) constraint).regex.toString(), true);
        }

        throw new UnsupportedOperationException("Unable to create a string generator for " + constraint.getClass().getName());
    }

    /**
     * @return All non-length related constraints
     */
    private Stream<AtomicConstraintState> getPatternConstraints() {
        return constraints.stream()
            .filter(c -> !(c.constraint instanceof StringHasLengthConstraint)
                && !(c.constraint instanceof IsStringShorterThanConstraint)
                && !(c.constraint instanceof IsStringLongerThanConstraint));
    }

    /**
     * Calculate the length the string must be longer than or if the constraints have created a contradiction.
     * If there are no minimum length constraints, then -1 will be returned, allowing for strings of 0 length or greater
     *
     * @return The length the string must be longer than
     */
    private Merged<Integer> getLongerThan() {
        int longerThan = 0;
        boolean longerThanSet = false;

        for (AtomicConstraintState state: this.constraints){
            if (state.negated){
                if (state.constraint instanceof IsStringShorterThanConstraint){
                    longerThan = Math.max(longerThan, ((IsStringShorterThanConstraint) state.constraint).referenceValue - 1);
                    longerThanSet = true;
                }

                continue;
            }

            if (state.constraint instanceof IsStringLongerThanConstraint){
                longerThan = Math.max(longerThan, ((IsStringLongerThanConstraint) state.constraint).referenceValue);
                longerThanSet = true;
            }
        }

        return longerThanSet ? Merged.of(longerThan) : Merged.of(-1);
    }

    /**
     * Calculate the length the string must be shorter than or if the constraints have created a contradiction
     * If there are no maximum length constraints, then DEFAULT_SHORTER_THAN (255) will be returned
     *
     * @return The length the string must be shorter than
     */
    private Merged<Integer> getShorterThan() {
        int shorterThan = MAX_SHORTER_THAN;
        boolean shorterThanSet = false;

        for (AtomicConstraintState state: this.constraints){
            if (state.negated){
                if (state.constraint instanceof IsStringLongerThanConstraint){
                    shorterThan = Math.min(shorterThan, ((IsStringLongerThanConstraint) state.constraint).referenceValue + 1);
                    shorterThanSet = true;
                }

                continue;
            }

            if (state.constraint instanceof IsStringShorterThanConstraint){
                shorterThan = Math.min(shorterThan, ((IsStringShorterThanConstraint) state.constraint).referenceValue);
                shorterThanSet = true;
            }
        }

        return shorterThanSet ? Merged.of(shorterThan) : Merged.of(DEFAULT_SHORTER_THAN);
    }

    /**
     * Calculate the length the string must be equal to or if the constraints have created a contradiction
     *
     * @return The length the string must be equal to or null
     */
    private Merged<Integer> getOfLength() {
        Integer ofLength = null;

        for (AtomicConstraintState state: this.constraints){
            if (state.negated){
                continue;
            }

            if (state.constraint instanceof StringHasLengthConstraint){
                int thisLength = ((StringHasLengthConstraint) state.constraint).referenceValue;
                if (ofLength == null){
                    ofLength = thisLength;
                } else if (ofLength != thisLength){
                    return Merged.contradictory();
                }
            }
        }

        return ofLength != null ? Merged.of(ofLength) : null;
    }

    /**
     * Determine all the constraints that define the lengths strings should not be equal to, so they can be excluded
     *
     * @return A set of string lengths that should not be created
     */
    private Set<Integer> getExcludedLengths() {
        Set<Integer> excludedLengths = new HashSet<>();

        for (AtomicConstraintState state: this.constraints){
            if (state.negated){
                if (state.constraint instanceof StringHasLengthConstraint){
                    //not sure what should happen here, not(ofLength xx)
                    int thisLength = ((StringHasLengthConstraint) state.constraint).referenceValue;
                    excludedLengths.add(thisLength);
                }
            }
        }

        return excludedLengths;
    }

    @Override
    public String toString() {
        return constraints
            .stream()
            .map(AtomicConstraintState::toString)
            .collect(Collectors.joining(", "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringRestrictions that = (StringRestrictions) o;

        return constraints.equals(that.constraints);
    }

    @Override
    public int hashCode() {
        return constraints.hashCode();
    }
}

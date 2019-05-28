package com.scottlogic.deg.generator.generation;

import com.scottlogic.deg.generator.utils.JavaUtilRandomNumberGenerator;
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import com.scottlogic.deg.generator.utils.SupplierBasedIterator;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexStringGenerator implements StringGenerator {
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

    /**
     * Cache of all matching regex automatons, keyed on their regex
     */
    private static final Map<String, Automaton> matchingRegexAutomatonCache = new HashMap<>();

    /**
     * Cache of all containing regex automatons, keyed on their regex
     */
    private static final Map<String, Automaton> containingRegexAutomatonCache = new HashMap<>();

    private Automaton automaton;
    private Node rootNode;
    private boolean isRootNodeBuilt;
    private int preparedTransactionNode;
    private final String regexRepresentation;

    public RegexStringGenerator(String regexStr, boolean matchFullString) {
        Map<String, Automaton> cache = matchFullString ? matchingRegexAutomatonCache : containingRegexAutomatonCache;
        Automaton generatedAutomaton = cache.containsKey(regexStr)
            ? cache.get(regexStr)
            : createAutomaton(regexStr, matchFullString, cache);

        String prefix = matchFullString ? "" : "*";
        String suffix = matchFullString ? "" : "*";
        this.regexRepresentation = String.format("%s/%s/%s", prefix, regexStr, suffix);
        this.automaton = generatedAutomaton;
    }

    /**
     * Create an automaton and store its instance in the cache, keyed on the given regex
     * The cache will vary based on &lt;matchFullString&gt;.
     *
     * The creation of an automaton is a time-consuming process, especially for more complex expressions.
     *
     * @param regexStr The string to create the automaton from
     * @param matchFullString Whether the string represents a matchingRegex (true) or containingRegex (false) expression
     * @param cache The cache to store the automaton instance in
     * @return The created automaton
     */
    static Automaton createAutomaton(String regexStr, boolean matchFullString, Map<String, Automaton> cache) {
        final String anchoredStr = convertEndAnchors(regexStr, matchFullString);
        final String requotedStr = escapeCharacters(anchoredStr);
        final RegExp bricsRegExp = expandShorthandClasses(requotedStr);

        Automaton generatedAutomaton = bricsRegExp.toAutomaton();
        generatedAutomaton.expandSingleton();

        cache.put(regexStr, generatedAutomaton);
        return generatedAutomaton;
    }

    private RegexStringGenerator(Automaton automaton, String regexRepresentation) {
        this.automaton = automaton;
        this.regexRepresentation = regexRepresentation;
    }

    @Override
    public String toString() {
        if (regexRepresentation != null)
            return regexRepresentation;

        if (this.automaton != null)
            return this.automaton.toString();

        return "<UNKNOWN>";
    }

    public static RegexStringGenerator createFromBlacklist(Set<Object> blacklist) {
        return RegexStringGenerator.createFromBlacklist(blacklist, new JavaUtilRandomNumberGenerator());
    }

    public static RegexStringGenerator createFromBlacklist(Set<Object> blacklist, RandomNumberGenerator random) {
        String[] blacklistStrings = blacklist.stream().map(Object::toString).toArray(String[]::new);
        Automaton automaton = Automaton.makeStringUnion(blacklistStrings).complement();

        return new RegexStringGenerator(automaton, String.format("NOT-IN %s", Objects.toString(blacklist)));
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
        String mergedRepresentation = intersectRepresentation(this.regexRepresentation, otherRegexGenerator.regexRepresentation);

        return new RegexStringGenerator(merged, mergedRepresentation);
    }

    public RegexStringGenerator union(RegexStringGenerator otherGenerator) {
        Automaton b = otherGenerator.automaton;
        Automaton merged = automaton.union(b);
        String mergedRepresentation = unionRepresentation(
            this.regexRepresentation,
            otherGenerator.regexRepresentation
        );
        return new RegexStringGenerator(merged, mergedRepresentation);
    }

    @Override
    public StringGenerator complement() {
        return new RegexStringGenerator(
            this.automaton.clone().complement(),
            complementaryRepresentation(this.regexRepresentation));
    }

    private static String complementaryRepresentation(String representation) {
        return String.format("¬(%s)", representation);
    }

    static String intersectRepresentation(String left, String right) {
        return String.format("(%s ∩ %s)", left, right);
    }

    static String unionRepresentation(String left, String right) {
        return String.format("(%s ∪ %s)", left, right);
    }

    @Override
    public boolean isFinite() {
        return automaton.isFinite();
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        try {
            String shortestString = AutomatonUtils.getShortestExample(automaton);
            String longestString = AutomatonUtils.getLongestExample(automaton);

            return shortestString.equals(longestString)
                ? Collections.singleton(shortestString)
                : Arrays.asList(shortestString, longestString);
        } catch (Exception e) {
            System.err.println(
                String.format(
                    "Unable to generate interesting strings for %s\n%s",
                    this.regexRepresentation,
                    e.getMessage()));

            return Collections.emptySet();
        }
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (this.isFinite()) {
            return () -> new RegexStringGenerator.FiniteStringAutomatonIterator(this);
        }

        // TODO: Assess whether we can do better here. Is it unacceptable to just generate indefinitely?
        // We used to generate randomly, but that violates a reasonable expectation that values returned by this method should be unique
        throw new UnsupportedOperationException("Can't generate all strings for a non-finite regex");
    }

    @Override
    public Iterable<String> generateRandomValues(RandomNumberGenerator randomNumberGenerator) {
        return () -> new SupplierBasedIterator<>(
            () -> generateRandomStringInternal(
                "",
                automaton.getInitialState(),
                1,
                Integer.MAX_VALUE,
                randomNumberGenerator));
    }

    private String getMatchedString(int indexOrder) {
        buildRootNode();
        if (indexOrder < 1)
            throw new IllegalArgumentException("indexOrder must be >= 1");

        if (indexOrder > rootNode.matchedStringIdx) {
            return null;
        }
        String result = buildStringFromNode(rootNode, indexOrder);
        result = result.substring(1, result.length() - 1);
        return result;
    }

    @Override
    public long getValueCount() {
        if (!this.isFinite()) {
            throw new UnsupportedOperationException("Cannot count matches for a non-finite expression.");
        }

        buildRootNode();

        if (rootNode.nextNodes.isEmpty()) {
            return 0;
        }

        return rootNode.matchedStringIdx;
    }

    @Override
    public boolean match(String subject) {

        return automaton.run(subject);

    }

    private static String escapeCharacters(String regex) {
        final Pattern patternRequoted = Pattern.compile("\\\\Q(.*?)\\\\E");
        final Pattern patternSpecial = Pattern.compile("[.^$*+?(){|\\[\\\\@]");
        StringBuilder sb = new StringBuilder(regex);
        Matcher matcher = patternRequoted.matcher(sb);
        while (matcher.find()) {
            sb.replace(matcher.start(), matcher.end(), patternSpecial.matcher(matcher.group(1)).replaceAll("\\\\$0"));
        }
        return sb.toString();
    }

    private static String convertEndAnchors(String regexStr, boolean matchFullString) {
        final Matcher startAnchorMatcher = Pattern.compile("^\\^").matcher(regexStr);

        if (startAnchorMatcher.find()) {
            regexStr = startAnchorMatcher.replaceAll(""); // brics.RegExp doesn't use anchors - they're treated as literal ^/$ characters
        } else if (!matchFullString) {
            regexStr = ".*" + regexStr; // brics.RegExp only supports full string matching, so add .* to simulate it
        }

        final Matcher endAnchorMatcher = Pattern.compile("\\$$").matcher(regexStr);

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

    private String generateRandomStringInternal(
        String strMatch,
        State state,
        int minLength,
        int maxLength,
        RandomNumberGenerator random) {

        List<Transition> transitions = state.getSortedTransitions(false);
        Set<Integer> selectedTransitions = new HashSet<>();
        String result = strMatch;

        for (int resultLength = -1;
             transitions.size() > selectedTransitions.size()
                 && (resultLength < minLength || resultLength > maxLength);
             resultLength = result.length()) {

            if (randomPrepared(strMatch, state, minLength, maxLength, transitions, random)) {
                return strMatch;
            }

            int nextInt = random.nextInt(transitions.size());
            if (!selectedTransitions.contains(nextInt)) {
                selectedTransitions.add(nextInt);


                /**
                 * <p>
                 * We have to surround this functionality in a loop checking for invalid
                 * UTF-8 characters until the automaton library is updated.
                 * </p>
                 * <p>
                 * FIXME - This check will be removed if/when the dk.brics.automaton
                 * library is fixed to support surrogate pairs,
                 * </p>
                 * <p>
                 * issue #15 (https://github.com/cs-au-dk/dk.brics.automaton/issues/15)
                 * has been raised on the dk.brics.automaton library
                 * </p>
                 * <p>
                 * issue #537 has been created to track when the dk.brics.automaton library
                 * is updated.
                 * </p>
                 */
                Transition randomTransition;
                char randomChar;
                do {
                    randomTransition = transitions.get(nextInt);
                    int diff = randomTransition.getMax() - randomTransition.getMin() + 1;
                    int randomOffset = diff > 0 ? random.nextInt(diff) : diff;
                    randomChar = (char) (randomOffset + randomTransition.getMin());
                } while (!isCharValidUtf8(randomChar));
                result = generateRandomStringInternal(strMatch + randomChar, randomTransition.getDest(), minLength, maxLength, random);
            }
        }

        return result;
    }

    private boolean randomPrepared(
        String strMatch,
        State state,
        int minLength,
        int maxLength,
        List<Transition> transitions,
        RandomNumberGenerator random) {

        if (state.isAccept()) {
            if (strMatch.length() == maxLength) {
                return true;
            }
            if (random.nextInt() > 0.3 * Integer.MAX_VALUE && strMatch.length() >= minLength) {
                return true;
            }
        }

        return transitions.size() == 0;
    }

    private String buildStringFromNode(RegexStringGenerator.Node node, int indexOrder) {
        String result = "";
        long passedStringNbr = 0;
        long step = node.getMatchedStringIdx() / node.getNbrChar();
        for (char usedChar = node.getMinChar(); usedChar <= node.getMaxChar(); ++usedChar) {
            passedStringNbr += step;
            if (passedStringNbr >= indexOrder) {
                passedStringNbr -= step;
                indexOrder -= passedStringNbr;
                result = result.concat("" + usedChar);
                break;
            }
        }
        long passedStringNbrInChildNode = 0;
        if (result.length() == 0)
            passedStringNbrInChildNode = passedStringNbr;
        for (RegexStringGenerator.Node childN : node.getNextNodes()) {
            passedStringNbrInChildNode += childN.getMatchedStringIdx();
            if (passedStringNbrInChildNode >= indexOrder) {
                passedStringNbrInChildNode -= childN.getMatchedStringIdx();
                indexOrder -= passedStringNbrInChildNode;
                result = result.concat(buildStringFromNode(childN, indexOrder));
                break;
            }
        }
        return result;
    }

    private void buildRootNode() {

        if (isRootNodeBuilt)
            return;
        isRootNodeBuilt = true;

        rootNode = new RegexStringGenerator.Node();
        List<RegexStringGenerator.Node> nextNodes = prepareTransactionNodes(automaton.getInitialState());
        rootNode.setNextNodes(nextNodes);
        rootNode.updateMatchedStringIdx();
    }

    private List<RegexStringGenerator.Node> prepareTransactionNodes(State state) {

        List<RegexStringGenerator.Node> transactionNodes = new ArrayList<>();
        if (preparedTransactionNode == Integer.MAX_VALUE / 2) {
            return transactionNodes;
        }
        ++preparedTransactionNode;

        if (state.isAccept()) {
            RegexStringGenerator.Node acceptedNode = new RegexStringGenerator.Node();
            acceptedNode.setNbrChar(1);
            transactionNodes.add(acceptedNode);
        }
        List<Transition> transitions = state.getSortedTransitions(true);

        //System.out.println(">" + state.toString());
//        if (transitions.size() > 1) {
//            System.out.println(">" + transitions.get(0).getMin());
//        }

        for (Transition transition : transitions) {
            RegexStringGenerator.Node trsNode = new RegexStringGenerator.Node();
            int nbrChar = transition.getMax() - transition.getMin() + 1;
            trsNode.setNbrChar(nbrChar);
            trsNode.setMaxChar(transition.getMax());
            trsNode.setMinChar(transition.getMin());
            List<RegexStringGenerator.Node> nextNodes = prepareTransactionNodes(transition.getDest());
            trsNode.setNextNodes(nextNodes);
            transactionNodes.add(trsNode);
        }
        return transactionNodes;
    }

    private class Node {
        private int nbrChar = 1;
        private List<RegexStringGenerator.Node> nextNodes = new ArrayList<>();
        private boolean isNbrMatchedStringUpdated;
        private long matchedStringIdx = 0;
        private char minChar;
        private char maxChar;

        int getNbrChar() {
            return nbrChar;
        }

        void setNbrChar(int nbrChar) {
            this.nbrChar = nbrChar;
        }

        List<RegexStringGenerator.Node> getNextNodes() {
            return nextNodes;
        }

        void setNextNodes(List<RegexStringGenerator.Node> nextNodes) {
            this.nextNodes = nextNodes;
        }

        void updateMatchedStringIdx() {
            if (isNbrMatchedStringUpdated) {
                return;
            }
            if (nextNodes.size() == 0) {
                matchedStringIdx = nbrChar;
            } else {
                for (RegexStringGenerator.Node childNode : nextNodes) {
                    childNode.updateMatchedStringIdx();
                    long childNbrChar = childNode.getMatchedStringIdx();
                    matchedStringIdx += nbrChar * childNbrChar;
                }
            }
            isNbrMatchedStringUpdated = true;
        }

        long getMatchedStringIdx() {
            return matchedStringIdx;
        }

        char getMinChar() {
            return minChar;
        }

        void setMinChar(char minChar) {
            this.minChar = minChar;
        }

        char getMaxChar() {
            return maxChar;
        }

        void setMaxChar(char maxChar) {
            this.maxChar = maxChar;
        }
    }

    private class FiniteStringAutomatonIterator implements Iterator<String> {

        private final RegexStringGenerator stringGenerator;
        private final long matches;
        private int currentIndex;
        private String currentValue;

        FiniteStringAutomatonIterator(RegexStringGenerator stringGenerator) {
            this.stringGenerator = stringGenerator;
            this.matches = stringGenerator.getValueCount();
            currentIndex = 0;
        }

        /**
         * <p>
         * This function has been updated to only allow valid single 16-bit
         * word UTF-8 characters to be output.
         * </p>
         * <p>
         * FIXME - This check will be removed if/when the dk.brics.automaton
         * library is fixed to support surrogate pairs,
         * </p>
         * <p>
         * issue #15 (https://github.com/cs-au-dk/dk.brics.automaton/issues/15)
         * has been raised on the dk.brics.automaton library
         * </p>
         * <p>
         * issue #537 has been created to track when the dk.brics.automaton library
         * is updated.
         * </p>
         * @return true if another value is available, false if all valid values have been read.
         */
        @Override
        public boolean hasNext() {
            if (currentValue != null) {
                return true;
            }
            do {
                currentIndex++; // starts at 1
                if (currentIndex > matches) {
                    return false;
                }
                currentValue = stringGenerator.getMatchedString(currentIndex);
            } while (!isStringValidUtf8(currentValue));
            return currentValue != null;
        }

        @Override
        public String next() {
            try {
                return currentValue;
            } finally {
                currentValue = null;
            }
        }
    }

    /**
     * <p>
     * check to see if the character generated is a valid utf-8 single word value.
     * </p>
     * <p>
     * from chapter 3.9, page 126 of `the Unicode Standard v11.0`
     * (https://www.unicode.org/versions/Unicode11.0.0/ch02.pdf):
     * </p>
     * <code>Because surrogate code points are not Unicode scalar values, any UTF-8 byte
     * sequence that would otherwise map to code points U+D800..U+DFFF is illformed.
     * </code>
     * @return true if the string parameter contains valid plane 0 unicode characters.
     *         false if it contains any surrogate characters.
     */
    public boolean isStringValidUtf8(String str) {
        for (char c : str.toCharArray()) {
            if (!isCharValidUtf8(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isCharValidUtf8(char c) {
        return !Character.isSurrogate(c);
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

    public static class UnionCollector
    {
        private RegexStringGenerator union = null;

        public UnionCollector() { }

        public void accumulate(RegexStringGenerator another) {
            if (union == null) {
                union = another;
            }
            else {
                union = union.union(another);
            }
        }

        public void combine(UnionCollector other) {
            if (other == null || other.union == null) {
                return;
            }
            union = union.union(other.union);
        }

        public RegexStringGenerator getUnionGenerator() {
            return union;
        }
    }
}


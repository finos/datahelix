package com.scottlogic.deg.generator.utils;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringGenerator implements IStringGenerator {

    private static final Map<String, String> PREDEFINED_CHARACTER_CLASSES;

    public static StringGenerator createFromBlacklist(Set<Object> blacklist) {
        return StringGenerator.createFromBlacklist(blacklist, new RandomGenerator());
    }

    public static StringGenerator createFromBlacklist(Set<Object> blacklist, IRandomGenerator random) {
        String[] blacklistStrings = new String[blacklist.size()];
        int i = 0;
        for (Object obj : blacklist) {
            blacklistStrings[i] = obj.toString();
        }
        Automaton automaton = Automaton.makeStringUnion(blacklistStrings).complement();

        return new StringGenerator(automaton, random);
    }

    private IRandomGenerator random;
    private Automaton automaton;
    private Node rootNode;
    private boolean isRootNodeBuilt;

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

    public StringGenerator(String regexStr, IRandomGenerator random) {
        final String requotedStr = escapeCharacters(regexStr);
        final RegExp bricsRegExp = expandShorthandClasses(requotedStr);

        Automaton generatedAutomaton = bricsRegExp.toAutomaton();
        generatedAutomaton.expandSingleton();

        this.automaton = generatedAutomaton;
        this.random = random;
    }

    public StringGenerator(String regexStr) {

        this(regexStr, new RandomGenerator());
    }

    private StringGenerator(Automaton automaton, IRandomGenerator random) {
        this.automaton = automaton;
        this.random = random;
    }

    @Override
    public IStringGenerator intersect(IStringGenerator stringGenerator) {
        Automaton b = ((StringGenerator) stringGenerator).automaton;
        Automaton merged = automaton.intersection(b);

        return new StringGenerator(merged, this.random);
    }

    @Override
    public IStringGenerator complement() {
        return new StringGenerator(this.automaton.clone().complement(), this.random);
    }

    @Override
    public boolean IsFinite() {
        return automaton.isFinite();
    }

    @Override
    public boolean canProduceValues() {
        return automaton.getNumberOfStates() > 0;
    }

    @Override
    public Iterator<String> generateAllValues() {
        return this.IsFinite()
                ? new StringGenerator.FiniteStringAutomatonIterator(this)
                : new StringGenerator.InfiniteStringAutomatonIterator(this);
    }

    @Override
    public String generateRandomValue() {
        return generateRandomStringInternal("", automaton.getInitialState(), 1, Integer.MAX_VALUE);
    }

    @Override
    public String generateRandomValue(int maxChars) {
        return generateRandomStringInternal("", automaton.getInitialState(), 1, maxChars);
    }

    @Override
    public String getMatchedString(int indexOrder) {
        buildRootNode();
        if (indexOrder == 0)
            indexOrder = 1;

        if (indexOrder > rootNode.matchedStringIdx) {
            return null;
        }
        String result = buildStringFromNode(rootNode, indexOrder);
        result = result.substring(1, result.length() - 1);
        return result;
    }

    @Override
    public long getValueCount() {
        if (!this.IsFinite()) {
            throw new UnsupportedOperationException("Cannot count matches for a non-finite expression.");
        }

        buildRootNode();

        return rootNode.matchedStringIdx;
    }

    private String escapeCharacters(String regex) {
        final Pattern patternRequoted = Pattern.compile("\\\\Q(.*?)\\\\E");
        final Pattern patternSpecial = Pattern.compile("[.^$*+?(){|\\[\\\\@]");
        StringBuilder sb = new StringBuilder(regex);
        Matcher matcher = patternRequoted.matcher(sb);
        while (matcher.find()) {
            sb.replace(matcher.start(), matcher.end(), patternSpecial.matcher(matcher.group(1)).replaceAll("\\\\$0"));
        }
        return sb.toString();
    }

    /*
     * As the Briks regex parser doesn't recognise shorthand classes we need to convert them to character groups
     */
    private RegExp expandShorthandClasses(String regex) {
        String finalRegex = regex;
        for (Map.Entry<String, String> charClass : PREDEFINED_CHARACTER_CLASSES.entrySet()) {
            finalRegex = finalRegex.replaceAll(charClass.getKey(), charClass.getValue());
        }
        return new RegExp(finalRegex);
    }

    private String generateRandomStringInternal(String strMatch, State state, int minLength, int maxLength) {
        List<Transition> transitions = state.getSortedTransitions(false);
        Set<Integer> selectedTransitions = new HashSet<>();
        String result = strMatch;

        for (int resultLength = -1;
             transitions.size() > selectedTransitions.size()
                     && (resultLength < minLength || resultLength > maxLength);
             resultLength = result.length()) {

            if (randomPrepared(strMatch, state, minLength, maxLength, transitions)) {
                return strMatch;
            }

            int nextInt = random.nextInt(transitions.size());
            if (!selectedTransitions.contains(nextInt)) {
                selectedTransitions.add(nextInt);

                Transition randomTransition = transitions.get(nextInt);
                int diff = randomTransition.getMax() - randomTransition.getMin() + 1;
                int randomOffset = diff > 0 ? random.nextInt(diff) : diff;
                char randomChar = (char) (randomOffset + randomTransition.getMin());
                result = generateRandomStringInternal(strMatch + randomChar, randomTransition.getDest(), minLength, maxLength);
            }
        }

        return result;
    }

    private boolean randomPrepared(
            String strMatch,
            State state,
            int minLength,
            int maxLength,
            List<Transition> transitions) {

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

    private String buildStringFromNode(StringGenerator.Node node, int indexOrder) {
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
        for (StringGenerator.Node childN : node.getNextNodes()) {
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

    private int preparedTransactionNode;

    private void buildRootNode() {

        if (isRootNodeBuilt)
            return;
        isRootNodeBuilt = true;

        rootNode = new StringGenerator.Node();
        List<StringGenerator.Node> nextNodes = prepareTransactionNodes(automaton.getInitialState());
        rootNode.setNextNodes(nextNodes);
        rootNode.updateMatchedStringIdx();
    }

    private List<StringGenerator.Node> prepareTransactionNodes(State state) {

        List<StringGenerator.Node> transactionNodes = new ArrayList<>();
        if (preparedTransactionNode == Integer.MAX_VALUE / 2) {
            return transactionNodes;
        }
        ++preparedTransactionNode;

        if (state.isAccept()) {
            StringGenerator.Node acceptedNode = new StringGenerator.Node();
            acceptedNode.setNbrChar(1);
            transactionNodes.add(acceptedNode);
        }
        List<Transition> transitions = state.getSortedTransitions(true);
        for (Transition transition : transitions) {
            StringGenerator.Node trsNode = new StringGenerator.Node();
            int nbrChar = transition.getMax() - transition.getMin() + 1;
            trsNode.setNbrChar(nbrChar);
            trsNode.setMaxChar(transition.getMax());
            trsNode.setMinChar(transition.getMin());
            List<StringGenerator.Node> nextNodes = prepareTransactionNodes(transition.getDest());
            trsNode.setNextNodes(nextNodes);
            transactionNodes.add(trsNode);
        }
        return transactionNodes;
    }

    private class Node {
        private int nbrChar = 1;
        private List<StringGenerator.Node> nextNodes = new ArrayList<>();
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

        List<StringGenerator.Node> getNextNodes() {
            return nextNodes;
        }

        void setNextNodes(List<StringGenerator.Node> nextNodes) {
            this.nextNodes = nextNodes;
        }

        void updateMatchedStringIdx() {
            if (isNbrMatchedStringUpdated) {
                return;
            }
            if (nextNodes.size() == 0) {
                matchedStringIdx = nbrChar;
            } else {
                for (StringGenerator.Node childNode : nextNodes) {
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

        private final StringGenerator stringGenerator;
        private int currentIndex;

        FiniteStringAutomatonIterator(StringGenerator stringGenerator) {
            this.stringGenerator = stringGenerator;
            currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            long matches = stringGenerator.getValueCount();
            return currentIndex < matches;
        }

        @Override
        public String next() {
            currentIndex++; // starts at 1
            return stringGenerator.getMatchedString(currentIndex);
        }
    }

    private class InfiniteStringAutomatonIterator implements Iterator<String> {

        private final StringGenerator stringGenerator;

        InfiniteStringAutomatonIterator(StringGenerator stringGenerator) {
            this.stringGenerator = stringGenerator;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public String next() {
            return stringGenerator.generateRandomValue();
        }
    }
}

package com.scottlogic.deg.generator.utils;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RegexStringGenerator implements IStringGenerator {
    private static final Map<String, String> PREDEFINED_CHARACTER_CLASSES;

    public static RegexStringGenerator createFromBlacklist(Set<Object> blacklist) {
        return RegexStringGenerator.createFromBlacklist(blacklist, new JavaUtilRandomNumberGenerator());
    }

    public static RegexStringGenerator createFromBlacklist(Set<Object> blacklist, IRandomNumberGenerator random) {
        String[] blacklistStrings = new String[blacklist.size()];
        int i = 0;
        for (Object obj : blacklist) {
            blacklistStrings[i] = obj.toString();
        }
        Automaton automaton = Automaton.makeStringUnion(blacklistStrings).complement();

        return new RegexStringGenerator(automaton);
    }

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

    public RegexStringGenerator(String regexStr) {
        final String requotedStr = escapeCharacters(regexStr);
        final RegExp bricsRegExp = expandShorthandClasses(requotedStr);

        Automaton generatedAutomaton = bricsRegExp.toAutomaton();
        generatedAutomaton.expandSingleton();

        this.automaton = generatedAutomaton;
    }

    private RegexStringGenerator(Automaton automaton) {
        this.automaton = automaton;
    }

    @Override
    public IStringGenerator intersect(IStringGenerator stringGenerator) {
        Automaton b = ((RegexStringGenerator) stringGenerator).automaton;
        Automaton merged = automaton.intersection(b);

        return new RegexStringGenerator(merged);
    }

    @Override
    public IStringGenerator complement() {
        return new RegexStringGenerator(this.automaton.clone().complement());
    }

    @Override
    public boolean isFinite() {
        return automaton.isFinite();
    }

    @Override
    public Iterable<String> generateInterestingValues() {
        return () -> generateInterestingValues(automaton.getInitialState(), "").iterator();
    }

    private Stream<String> generateInterestingValues(State state, String matchedValue) {
        Stream<String> matchedValueStream = state.isAccept()
            ? Stream.of(matchedValue)
            : Stream.empty();

        Stream<String> subTransitionStream = state
            .getTransitions()
            .stream()
            .flatMap(transition ->
                generateInterestingValues(transition.getDest(), matchedValue + transition.getMin()));

        return Stream.concat(matchedValueStream, subTransitionStream);
    }

    @Override
    public Iterable<String> generateAllValues() {
        if (this.isFinite())
            return () -> new RegexStringGenerator.FiniteStringAutomatonIterator(this);

        // TODO: Assess whether we can do better here. Is it unacceptable to just generate indefinitely?
        // We used to generate randomly, but that violates a reasonable expectation that values returned by this method should be unique
        throw new UnsupportedOperationException("Can't generate all strings for a non-finite regex");
    }

    @Override
    public Iterable<String> generateRandomValues(IRandomNumberGenerator randomNumberGenerator) {
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

        return rootNode.matchedStringIdx;
    }

    @Override
    public boolean match(String subject) {

        return automaton.run(subject);

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

    private String generateRandomStringInternal(
            String strMatch,
            State state,
            int minLength,
            int maxLength,
            IRandomNumberGenerator random) {

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

                Transition randomTransition = transitions.get(nextInt);
                int diff = randomTransition.getMax() - randomTransition.getMin() + 1;
                int randomOffset = diff > 0 ? random.nextInt(diff) : diff;
                char randomChar = (char) (randomOffset + randomTransition.getMin());
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
            IRandomNumberGenerator random) {

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

    private int preparedTransactionNode;

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
        private int currentIndex;

        FiniteStringAutomatonIterator(RegexStringGenerator stringGenerator) {
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
}

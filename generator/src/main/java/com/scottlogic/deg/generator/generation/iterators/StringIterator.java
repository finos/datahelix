package com.scottlogic.deg.generator.generation.iterators;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;

public class StringIterator implements IFieldSpecIterator {
    private Random random = new Random(0);
    private Automaton automaton;
    private Node rootNode;
    private int preparedTransactionNode;
    private boolean randomResults;
    private int nextMatchIdx = 1;
    private String nextMatchValue;
    private boolean hasNext;

    public StringIterator(Automaton automaton, Set<Object> blacklist) {
        if (blacklist != null && blacklist.size() > 0) {
            Automaton blacklistAutomaton = getBlacklistAutomaton(blacklist);
            this.automaton = automaton.intersection(blacklistAutomaton);
        }
        else {
            this.automaton = automaton;
        }

        randomResults = isInfinite();
        if (this.automaton.isEmpty()) {
            hasNext = false;
        }
        else {
            hasNext = true;
            if (!isInfinite())
                buildRootNode();
            setNextMatchValue();
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Object next() {
        if (!hasNext) {
            return null;
        }
        String rv = nextMatchValue;
        nextMatchIdx++;
        setNextMatchValue();
        return rv;
    }

    private Automaton getBlacklistAutomaton(Set<Object> blacklist) {
        String[] blacklistStrings = new String[blacklist.size()];
        int i = 0;
        for (Object obj : blacklist) {
            blacklistStrings[i] = obj.toString();
        }
        return Automaton.makeStringUnion(blacklistStrings).complement();
    }

    @Override
    public boolean isInfinite() {
        return !automaton.isFinite();
    }

    private void setNextMatchValue() {
        if (!randomResults) {
            nextMatchValue = getOrderedString(nextMatchIdx);
            if (nextMatchValue.equals("") && nextMatchIdx > 1) {
                hasNext = false;
            }
        }
        else {
            nextMatchValue = getRandomString();
        }
    }

    private String getOrderedString(int indexOrder) {
        String result = buildStringFromNode(rootNode, indexOrder);
        result = result.substring(1, result.length() - 1);
        return result;
    }

    private String getRandomString() {
        return prepareRandom("", automaton.getInitialState(), 1, Integer.MAX_VALUE);
    }

    private String prepareRandom(String strMatch, State state, int minLength, int maxLength) {
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
                result = prepareRandom(strMatch + randomChar, randomTransition.getDest(), minLength, maxLength);
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

    private String buildStringFromNode(Node node, int indexOrder) {
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
        for (Node childN : node.getNextNodes()) {
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
        rootNode = new Node();
        List<Node> nextNodes = prepareTransactionNodes(automaton.getInitialState());
        rootNode.setNextNodes(nextNodes);
        rootNode.updateMatchedStringIdx();
    }

    private List<Node> prepareTransactionNodes(State state) {

        List<Node> transactionNodes = new ArrayList<>();
        if (preparedTransactionNode == Integer.MAX_VALUE / 2) {
            return transactionNodes;
        }
        ++preparedTransactionNode;

        if (state.isAccept()) {
            Node acceptedNode = new Node();
            acceptedNode.setNbrChar(1);
            transactionNodes.add(acceptedNode);
        }
        List<Transition> transitions = state.getSortedTransitions(true);
        for (Transition transition : transitions) {
            Node trsNode = new Node();
            int nbrChar = transition.getMax() - transition.getMin() + 1;
            trsNode.setNbrChar(nbrChar);
            trsNode.setMaxChar(transition.getMax());
            trsNode.setMinChar(transition.getMin());
            List<Node> nextNodes = prepareTransactionNodes(transition.getDest());
            trsNode.setNextNodes(nextNodes);
            transactionNodes.add(trsNode);
        }
        return transactionNodes;
    }

    private class Node {
        private int nbrChar = 1;
        private List<Node> nextNodes = new ArrayList<>();
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

        List<Node> getNextNodes() {
            return nextNodes;
        }

        void setNextNodes(List<Node> nextNodes) {
            this.nextNodes = nextNodes;
        }

        void updateMatchedStringIdx() {
            if (isNbrMatchedStringUpdated) {
                return;
            }
            if (nextNodes.size() == 0) {
                matchedStringIdx = nbrChar;
            }
            else {
                for (Node childNode : nextNodes) {
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
}

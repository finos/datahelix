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

package com.scottlogic.deg.generator.generation.string.factorys;

import com.scottlogic.deg.generator.generation.string.StringUtils;
import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.scottlogic.deg.common.util.NumberUtils.addingNonNegativesIsSafe;

public class FiniteStringAutomatonIterator implements Iterator<String> {
    private final long matches;
    private long currentIndex;
    private String currentValue;
    private Node rootNode;

    public FiniteStringAutomatonIterator(Automaton automaton) {
        rootNode = buildRootNode(automaton);
        matches = rootNode.getNextNodes().isEmpty() ? 0L : rootNode.getMatchedStringIdx();
        currentIndex = 0;
    }

    public Node buildRootNode(Automaton automaton) {
        rootNode = new Node();
        List<Node> nextNodes = prepareTransactionNodes(automaton.getInitialState(), 0);
        rootNode.setNextNodes(nextNodes);
        rootNode.updateMatchedStringIdx();
        return rootNode;
    }

    private List<Node> prepareTransactionNodes(State state, int preparedTransactionNode) {

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
            List<Node> nextNodes = prepareTransactionNodes(transition.getDest(), preparedTransactionNode);
            trsNode.setNextNodes(nextNodes);
            transactionNodes.add(trsNode);
        }
        return transactionNodes;
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
     *
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
            currentValue = getMatchedString(currentIndex);
        } while (currentValue != null && !StringUtils.isStringValidUtf8(currentValue));
        return currentValue != null;
    }

    private String getMatchedString(long indexOrder) {
        if (indexOrder < 1) {
            throw new IllegalArgumentException("indexOrder must be >= 1");
        }

        if (indexOrder > rootNode.getMatchedStringIdx()) {
            return null;
        }
        String result = buildStringFromNode(rootNode, indexOrder);
        result = result.substring(1, result.length() - 1);
        return result;
    }

    @Override
    public String next() {
        String result = currentValue;
        currentValue = null;
        return result;
    }

    private String buildStringFromNode(Node node, long indexOrder) {
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
        if (result.length() == 0) {
            passedStringNbrInChildNode = passedStringNbr;
        }
        for (Node childN : node.getNextNodes()) {
            long origNbrInChildNode = passedStringNbrInChildNode;
            if (addingNonNegativesIsSafe(passedStringNbrInChildNode, childN.getMatchedStringIdx())) {
                passedStringNbrInChildNode += childN.getMatchedStringIdx();
            } else {
                passedStringNbrInChildNode = Long.MAX_VALUE;
            }

            if (passedStringNbrInChildNode >= indexOrder) {
                passedStringNbrInChildNode = origNbrInChildNode;
                indexOrder -= passedStringNbrInChildNode;
                result = result.concat(buildStringFromNode(childN, indexOrder));
                break;
            }
        }
        return result;
    }

}

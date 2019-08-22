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

package com.scottlogic.deg.generator.generation.string.iterators;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;

import java.util.*;

public class FiniteStringAutomatonIterator implements Iterator<String> {
    private StringBuilder stringBuilder;
    private String nextValue;
    private Deque<Deque<TransitionIterator>> transitionStackStack;
    private Boolean firstStateIsAccept;

    public FiniteStringAutomatonIterator(Automaton automaton) {
        transitionStackStack = new ArrayDeque<>();
        firstStateIsAccept = automaton.getInitialState().isAccept();
        transitionStackStack.push(getTransitionsStack(automaton.getInitialState()));
        stringBuilder = new StringBuilder();
        nextValue = null;
    }

    private Deque<TransitionIterator> getTransitionsStack(State state) {
        Deque<TransitionIterator> transitions = new ArrayDeque<>();
        state
            .getSortedTransitions(true)
            .iterator()
            .forEachRemaining(transition -> transitions.addLast(new TransitionIterator(transition)));
        return transitions;
    }

    @Override
    public boolean hasNext() {

        while (true){
            if (nextValue != null) return true;
            else if (transitionStackStack.isEmpty()) return false;
            else if (firstStateIsAccept) {
                firstStateIsAccept = false;
                nextValue = "";
                return true;
            }
            else if (transitionStackStack.peek().isEmpty()){
                transitionStackStack.pop();
                if (stringBuilder.length() != 0) {
                    stringBuilder.deleteCharAt(stringBuilder.length()-1);
                }
            }
            else {
                TransitionIterator topTransitionIterator = transitionStackStack.peek().peek();

                if (topTransitionIterator.hasNext() && !topTransitionIterator.hasTransitions()) {
                    StringBuilder stringBuilderCopy = new StringBuilder(stringBuilder);
                    nextValue = stringBuilderCopy.append(topTransitionIterator.next()).toString();
                    return true;
                }
                else if (topTransitionIterator.hasNext() && topTransitionIterator.hasTransitions()) {
                    stringBuilder.append(topTransitionIterator.next());
                    transitionStackStack.push(getTransitionsStack(topTransitionIterator.getState()));

                    if (topTransitionIterator.isAccept()) {
                        topTransitionIterator.markAccept();
                        nextValue = stringBuilder.toString();
                        return true;
                    }
                }
                else if (!topTransitionIterator.hasNext()) {
                    transitionStackStack.peek().pop();
                }
            }
        }
    }

    @Override
    public String next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        String result = nextValue;
        nextValue = null;
        return result;
    }
}

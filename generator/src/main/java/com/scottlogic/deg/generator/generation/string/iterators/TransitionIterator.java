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

import dk.brics.automaton.State;
import dk.brics.automaton.Transition;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Iterator;
import java.util.function.Consumer;

public class TransitionIterator implements Iterator<Character> {
    private Transition transition;
    private Character currentChar;
    private Character maxChar;
    private Boolean accept;

    TransitionIterator(Transition transition) {
        this.transition = transition;
        currentChar = transition.getMin();
        maxChar = transition.getMax();
        resetAccept();
    }

    @Override
    public boolean hasNext() {
        return currentChar <= maxChar;
    }

    @Override
    public Character next() {
        resetAccept();
        return currentChar++;
    }

    boolean isAccept() {
        return accept;
    }

    void markAccept() {
        accept = false;
    }

    private void resetAccept() {
        accept = transition.getDest().isAccept();
    }

    boolean hasTransitions() {
        return !transition.getDest().getTransitions().isEmpty();
    }

    public State getState() {
        return transition.getDest();
    }
}

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
import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.List;

public class RandomStringFactory {

    public String createRandomString(
            String currentString,
            State state,
            int minLength,
            int maxLength,
            RandomNumberGenerator random) {

        if (finishCreating(currentString, state, minLength, maxLength, random)) {
            return currentString;
        }

        List<Transition> transitions = state.getSortedTransitions(false);

        Transition randomTransition = transitions.get(random.nextInt(transitions.size()));

        char randomChar = getRandomChar(random, randomTransition);

        return createRandomString(
            currentString + randomChar,
            randomTransition.getDest(),
            minLength,
            maxLength,
            random);
    }

    private boolean finishCreating(
            String currentString,
            State state,
            int minLength,
            int maxLength,
            RandomNumberGenerator random) {

        if (state.isAccept()) {
            if (currentString.length() == maxLength) {
                return true;
            }
            if (currentString.length() >= minLength && randomlyStop(random)) {
                return true;
            }
        }

        return state.getTransitions().isEmpty();
    }


    private boolean randomlyStop(RandomNumberGenerator random) {
        return random.nextInt(10) < 3; // 3 in 10 chance of stopping
    }

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
    private char getRandomChar(RandomNumberGenerator random, Transition randomTransition) {
        char randomChar;
        do {
            int diff = randomTransition.getMax() - randomTransition.getMin() + 1;
            randomChar = (char) (random.nextInt(diff) + randomTransition.getMin());
        } while (!StringUtils.isCharValidUtf8(randomChar));
        return randomChar;
    }
}

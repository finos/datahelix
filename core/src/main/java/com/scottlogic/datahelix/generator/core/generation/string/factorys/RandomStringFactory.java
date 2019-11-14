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

package com.scottlogic.datahelix.generator.core.generation.string.factorys;

import com.scottlogic.datahelix.generator.common.ValidationException;
import com.scottlogic.datahelix.generator.core.generation.string.StringUtils;
import com.scottlogic.datahelix.generator.common.RandomNumberGenerator;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.LinkedList;
import java.util.List;

public class RandomStringFactory {

    public String createRandomString(State state, RandomNumberGenerator random) {
        List<Integer> validIndices = new LinkedList<>();

        StringBuilder builder = new StringBuilder();

        if (state.isAccept()) {
            validIndices.add(0);
        }

        for (int i = 1; !finishCreating(state); i++) {
            List<Transition> transitions = state.getSortedTransitions(false);
            Transition randomTransition = transitions.get(random.nextInt(transitions.size()));
            builder.append(getRandomChar(random, randomTransition));
            state = randomTransition.getDest();
            if (state.isAccept()) {
                validIndices.add(i);
            }
        }

        if (validIndices.isEmpty()) {
            throw new ValidationException("No possible states from the current regex");
        }

        int randomIndex = random.nextInt(validIndices.size());
        return builder.toString().substring(0, validIndices.get(randomIndex));
    }

    private boolean finishCreating(State state) {
        return state.getTransitions().isEmpty();
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

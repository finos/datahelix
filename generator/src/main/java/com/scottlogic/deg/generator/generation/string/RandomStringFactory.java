package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.List;

public class RandomStringFactory {

    String createRandomString(
            String strMatch,
            State state,
            int minLength,
            int maxLength,
            RandomNumberGenerator random) {

        if (finishCreating(strMatch, state, minLength, maxLength, random)) {
            return strMatch;
        }

        List<Transition> transitions = state.getSortedTransitions(false);

        Transition randomTransition = transitions.get(random.nextInt(transitions.size()));

        char randomChar = getRandomChar(random, randomTransition);

        return createRandomString(
            strMatch + randomChar,
            randomTransition.getDest(),
            minLength,
            maxLength,
            random);
    }

    private boolean finishCreating(
            String strMatch,
            State state,
            int minLength,
            int maxLength,
            RandomNumberGenerator random) {

        if (state.isAccept()) {
            if (strMatch.length() == maxLength) {
                return true;
            }
            if (strMatch.length() >= minLength && randomlyStop(random)) {
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

package com.scottlogic.deg.generator.generation.string;

import com.scottlogic.deg.generator.utils.RandomNumberGenerator;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RandomStringFactory {

    String createRandomString(
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
                } while (!StringUtils.isCharValidUtf8(randomChar));
                result = createRandomString(
                    strMatch + randomChar,
                    randomTransition.getDest(),
                    minLength,
                    maxLength,
                    random);
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

        return transitions.isEmpty();
    }
}

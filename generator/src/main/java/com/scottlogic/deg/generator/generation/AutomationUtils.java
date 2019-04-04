package com.scottlogic.deg.generator.generation;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;
import java.util.stream.Collectors;

class AutomationUtils {

    private static final char printableChar = ' ';

    // Returns a string of printable characters based on the supplied automaton.
    // This method ignores transitions that lead back to the same node so recursive states will only ever produce a
    // single character. This means for infinite automatons the resulting string isn't the longest possible (as the
    // longest example would have an infinite length) but is based on the longest path from the start state to
    // the "furthest" end state.
    static String getLongestExample(Automaton automaton) {
        Transition transition = automaton.getInitialState().getSortedTransitions(true).get(0);
        TransitionLongestExampleCache cache = new TransitionLongestExampleCache();
        return getLongestExample(transition, cache);
    }

    private static class TransitionLongestExampleCache {
        private final Map<Transition, String> examples = new HashMap<>();

        String getLongestExample(Transition transition){
            if (examples.containsKey(transition)){
                return examples.get(transition);
            }

            String example = AutomationUtils.getLongestExample(transition, this);
            examples.put(transition, example);
            return example;
        }
    }

    /**
     * Recursive function for calculating the longest string that can be generated from the given regular automaton
     *
     * @param transition The transition to start at
     * @param cache A cache of previously resolved transitions
     * @return The longest string that can be generated from the given transition
     */
    private static String getLongestExample(Transition transition, TransitionLongestExampleCache cache){
        if (transition == null){
            throw new IllegalArgumentException("Transition cannot be null");
        }

        StringBuilder string = new StringBuilder();

        while (transition != null) { //need some exit condition
            State dest = transition.getDest();
            string.append((char)Math.max(printableChar, transition.getMin()));

            if (dest.isAccept() && dest.getTransitions().isEmpty()){
                return string.toString();
            }

            Map<State, List<Transition>> transitionsToUniqueStates = dest.getTransitions().stream().collect(Collectors.groupingBy(Transition::getDest));
            if (transitionsToUniqueStates.size() == 1){
                //every transition goes to the same destination, just use the first
                Transition nextTransition = transitionsToUniqueStates.values().iterator().next().get(0);

                if (nextTransition == transition){
                    //infinite generation
                    return string.toString();
                }

                transition = nextTransition;
                continue;
            }

            List<Transition> nextTransitions = transitionsToUniqueStates.values()
                .stream()
                .map(transitions -> transitions.get(0))
                .collect(Collectors.toList());

            //there are multiple suffix's, append the longest one
            String longestSuffix = "";
            for (Transition nextTransition: nextTransitions) {
                String suffix = cache.getLongestExample(nextTransition);
                if (suffix.length() > longestSuffix.length()){
                    longestSuffix = suffix;
                }
            }

            string.append(longestSuffix);
            return string.toString();
        }

        throw new IllegalStateException("Would only get here if transition is null, this should never happen");
    }

    // Taken from Automaton but updated to return printable characters
    static String getShortestExample(Automaton a) {
        State initialState = a.getInitialState();

        Map<State, String> stateToOutput = new HashMap<>();
        LinkedList<State> queue = new LinkedList<>();

        stateToOutput.put(initialState, "");
        queue.add(initialState);

        String currentBest = null;

        while (!queue.isEmpty()) {
            State currentState = queue.removeFirst();
            String currentOutput = stateToOutput.get(currentState);

            if (currentState.isAccept()) {

                if (currentBest == null
                    || currentOutput.length() < currentBest.length()
                    || (currentOutput.length() == currentBest.length() && currentOutput.compareTo(currentBest) < 0)) {
                    currentBest = currentOutput;
                }

            } else {
                for (Transition transition : currentState.getTransitions()) {
                    String nextOutput = stateToOutput.get(transition.getDest());
                    String nextOutputCalculated = currentOutput + (char)Math.max(transition.getMin(), printableChar);

                    if (nextOutput == null
                        || (nextOutput.length() == nextOutputCalculated.length() && nextOutputCalculated.compareTo(nextOutput) < 0)) {
                        if (nextOutput == null) {
                            queue.addLast(transition.getDest());
                        }
                        stateToOutput.put(transition.getDest(), nextOutputCalculated);
                    }
                }
            }
        }

        return currentBest;
    }
}

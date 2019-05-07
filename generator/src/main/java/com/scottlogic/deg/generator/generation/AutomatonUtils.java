package com.scottlogic.deg.generator.generation;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;
import java.util.stream.Collectors;

class AutomatonUtils {

    private static final char printableChar = ' ';

    /**
     * Get the longest string possible given the regex (in the form of the Automaton)
     * There may be many optional sections within a regex which need to be inspected to calculate the longest possible
     * string. The automaton has done the hard work of turning the regex into a set of transitions and states.
     * see https://github.com/ScottLogic/datahelix/blob/master/generator/docs/StringGeneration.md for more detail.
     *
     * This method is a recursive implementation that will test each starting point calculating the string and returning
     * the longest possible variant.
     *
     * TODO:
     * 1) turn this into a non-recursive algorithm
     * 2) improve performance and/or memory management (don't build strings as part of the process)
     *
     * @param automaton The automaton that represents the regex
     * @return The longest possible string for the given regex/automaton
     */
    static String getLongestExample(Automaton automaton) {
        List<Transition> transitions = automaton.getInitialState()
            .getSortedTransitions(true)
            .stream()
            .sorted(Comparator.comparing(Transition::getMin))
            .collect(Collectors.toList()); //ensure some form or determinism

        TransitionLongestExampleCache cache = new TransitionLongestExampleCache();

        String longest = "";
        for (Transition transition: transitions) {
            String transitionLongest = getLongestExample(transition, cache);
            if (transitionLongest.length() > longest.length()) {
                longest = transitionLongest;
            }
        }

        return longest;
    }

    /**
     * A cache of transitions to calculated strings, to save recalculating the string fragment for a transition multiple
     * times.
     */
    private static class TransitionLongestExampleCache {
        private final Map<Transition, String> examples = new HashMap<>();

        String getLongestExample(Transition transition){
            if (examples.containsKey(transition)){
                return examples.get(transition);
            }

            String example = AutomatonUtils.getLongestExample(transition, this);
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

            Map<State, List<Transition>> transitionsToUniqueStates = dest.getTransitions()
                .stream()
                .sorted(Comparator.comparing(Transition::getMin))
                .collect(Collectors.groupingBy(Transition::getDest));
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

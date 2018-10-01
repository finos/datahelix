package com.scottlogic.deg.generator.generation;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.State;
import dk.brics.automaton.Transition;

import java.util.*;
import java.util.stream.Collectors;

public class AutomationUtils {

    // Returns a string of printable characters based on the supplied automaton.
    // This method ignores transitions that lead back to the same node so recursive states will only ever produce a
    // single character. This means for infinite automatons the resulting string isn't the longest possible (as the
    // longest example would have an infinite length) but is based on the longest path from the start state to
    // the "furthest" end state.
    public static String getLongestExample(Automaton automaton) {

        Stack<Transition> solution = new Stack<Transition>();

        // The start node always has one transition to start
        Transition start = automaton.getInitialState().getSortedTransitions(true).get(0);

        visit(start, new Stack<>(), solution);

        StringBuilder sb = new StringBuilder();

        for (Transition transition : solution) {
            sb.append((char) Math.max(' ', transition.getMin()));
        }

        return sb.toString();

    }

    static void visit(Transition transition, Stack<Transition> ancestors, Stack<Transition> currentBest) {

        ancestors.push(transition);

        State dest = transition.getDest();

        List<Transition> filteredTransitions = dest.getTransitions()
            .stream()
            .filter(x -> x.getDest() != dest)
            .collect(Collectors.toList());

        if (dest.isAccept() && filteredTransitions.isEmpty() && ancestors.size() > currentBest.size()) {

            currentBest.clear();
            ancestors.forEach(currentBest::add);
        }

        for (Transition next : filteredTransitions) {
            visit(next, ancestors, currentBest);
        }

        ancestors.pop();
    }

    // Taken from Automaton but updated to return printable characters
    public static String getShortestExample(Automaton a) {
        State s = a.getInitialState();
        Map<State, String> path = new HashMap<State, String>();
        LinkedList<State> queue = new LinkedList<State>();
        path.put(s, "");
        queue.add(s);
        String best = null;
        while (!queue.isEmpty()) {
            State q = queue.removeFirst();
            String p = path.get(q);
            if (q.isAccept()) {
                if (best == null || p.length() < best.length() || (p.length() == best.length() && p.compareTo(best) < 0))
                    best = p;
            } else
                for (Transition t : q.getTransitions()) {
                    String tp = path.get(t.getDest());
                    String np = p + (char) Math.max(t.getMin(), ' ');
                    if (tp == null || (tp.length() == np.length() && np.compareTo(tp) < 0)) {
                        if (tp == null)
                            queue.addLast(t.getDest());
                        path.put(t.getDest(), np);
                    }
                }
        }
        return best;
    }

}

# String Generation

We use a Java library called [dk.brics.automaton](http://www.brics.dk/automaton/) to analyse regexes and generate valid (and invalid for [violation](DeliberateViolation.md)) strings based on them. It works by representing the regex as a finite state machine. It might be worth reading about state machines for those who aren't familiar: [https://en.wikipedia.org/wiki/Finite-state_machine](https://en.wikipedia.org/wiki/Finite-state_machine). Consider the following regex: `ABC[a-z]?(A|B)`. It would be represented by the following state machine:

![](finite-state-machine.svg)

<!-- graphvis dot file for the above graph
graph {
  rankdir="LR"
  s0[shape=circle]
  s1[shape=circle]
  s2[shape=circle]
  s3[shape=circle]
  s4[shape=circle]
  s5[shape=circle][fillcolor="green"][style="filled"]
  s6[shape=circle][fillcolor="green"][style="filled"]
  s7[shape=circle][fillcolor="green"][style="filled"]
  s8[shape=circle][fillcolor="green"][style="filled"]
  s0 -- s1[label="A"]
  s1 -- s2[label="B"]
  s2 -- s3[label="C"]
  s3 -- s4[label="[a-z]"]
  s3 -- s5[label="A"]  
  s3 -- s6[label="B"]  
  s4 -- s7[label="A"]  
  s4 -- s8[label="B"]  
}
-->

The [states](http://www.brics.dk/automaton/doc/index.html) (circular nodes) represent a string that may (green) or may not (white) be valid. For example, `s0` is the empty string, `s5` is `ABCA`.

The [transitions](http://www.brics.dk/automaton/doc/index.html) represent adding another character to the string. The characters allowed by a transition may be a range (as with `[a-z]`). A state does not store the string it represents itself, but is defined by the ordered list of transitions that lead to that state.

Another project that also uses dk.brics.automaton in a similar way to us might be useful to look at for further study: [https://github.com/mifmif/Generex](https://github.com/mifmif/Generex).

Other than the fact that we can use the state machine to generate strings, the main benefit that we get from using this library are:
* Finding the intersection of two regexes, used when there are multiple regex constraints on the same field.
* Finding the complement of a regex, which we use for generating invalid regexes for violation.

## Anchors

dk.brics.automaton doesn't support start and end anchors `^` & `$` and instead matches the entire word as if the anchors were always present. For some of our use cases though it may be that we want to match the regex in the middle of a string somewhere, so we have two versions of the regex constraint - [matchingRegex](https://github.com/ScottLogic/datahelix/blob/generator-documentation/docs/EpistemicConstraints.md#matchingregex-field-value) and [containingRegex](https://github.com/ScottLogic/datahelix/blob/generator-documentation/docs/EpistemicConstraints.md#containingregex-field-value). If `containingRegex` is used then we simply add a `.*` to the start and end of the regex before passing it into the automaton. Any `^` or `$` characters passed at the start or end of the string respectively are removed, as the automaton will treat them as literal characters.

## Character support

The generator does not support generating strings above the Basic Unicode plane (Plane 0). Using regexes that match characters above the basic plane may lead to unexpected behaviour.


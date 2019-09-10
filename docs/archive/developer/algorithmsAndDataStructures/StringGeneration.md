# String Generation

We use a Java library called [dk.brics.automaton](http://www.brics.dk/automaton/) to analyse regexes and generate valid (and invalid for [violation](../../user/alphaFeatures/DeliberateViolation.md)) strings based on them. It works by representing the regex as a finite state machine. It might be worth reading about state machines for those who aren't familiar: [https://en.wikipedia.org/wiki/Finite-state_machine](https://en.wikipedia.org/wiki/Finite-state_machine). Consider the following regex: `ABC[a-z]?(A|B)`. It would be represented by the following state machine:

![](../../user/images/finite-state-machine.svg)

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

Due to the way that the generator computes textual data internally the generation of strings is not deterministic and may output valid values in a different order with each generation run. 

## Anchors

dk.brics.automaton doesn't support start and end anchors `^` & `$` and instead matches the entire word as if the anchors were always present. For some of our use cases though it may be that we want to match the regex in the middle of a string somewhere, so we have two versions of the regex constraint - [matchingRegex](../../user/UserGuide.md#predicate-matchingregex) and [containingRegex](../../user/UserGuide.md#predicate-containingregex). If `containingRegex` is used then we simply add a `.*` to the start and end of the regex before passing it into the automaton. Any `^` or `$` characters passed at the start or end of the string respectively are removed, as the automaton will treat them as literal characters.

## Automaton data types
The automaton represents the state machine using the following types:
- `Transition`
- `State`

### `Transition`
A transition holds the following properties and are represented as lines in the above graph
- `min: char` - The minimum permitted character that can be emitted at this position
- `max: char` - The maximum permitted character that can be emitted at this position
- `to: State[]` - The `State`s that can follow this transition

In the above `A` looks like:

| property | initial | \[a-z\] | 
| ---- | ---- | ---- |
| min | A | a |
| max | A | z |
| to | 1 state, `s1` | 1 state, `s4` |

### `State`
A state holds the following properties and are represented as circles in the above graph
- `accept: boolean` - is this a termination state, can string production stop here?
- `transitions: HashSet<Transition>` - which transitions, if any, follow this state
- `number: int` - the number of this state
- `id: int` - the id of this state (not sure what this is used for)

In the above `s0` looks like:

| property | initial | s3 |
| ---- | ---- | ---- |
| accept | false | false |
| transitions | 1 transition, &rarr; `A` | 2 transitions:<br /> &rarr; `[a-z]`<br /> &rarr; `A|B` |
| number | 4 | 0 |
| id | 49 | 50 |

### Textual representation
The automaton can represent the state machine in a textual representation such as:

```
initial state: 4
state 0 [reject]:
  a-z -> 3
  A-B -> 1
state 1 [accept]:
state 2 [reject]:
  B -> 5
state 3 [reject]:
  A-B -> 1
state 4 [reject]:
  A -> 2
state 5 [reject]:
  C -> 0
```

This shows each state and each transition in the automaton, lines 2-4 show the `State` as shown in the previous section.
Lines 10-11 show the transition 'A' as shown in the prior section.

The pathway through the automaton is:
- transition to state **4** (because the initial - empty string state - is rejected/incomplete)
- add an 'A' (state 4)
- transition to state **2** (because the current state "A" is rejected/incomplete)
- add a 'B' (state 2)
- transition to state **5** (because the current state "AB" is rejected/incomplete)
- add a 'C' (state 5)
- transition to state **0** (because the current state "ABC" is rejected/incomplete)
   - _either_
      - add a letter between `a..z` (state 0, transition 1)
      - transition to state **3** (because the current state "ABCa" is rejected/incomplete)
      - add either 'A' or 'B' (state 3)
      - transition to state **1** (because the current state "ABCaA" is rejected/incomplete)
      - current state is accepted so exit with the current string "ABCaA"
   - _or_   
      - add either 'A' or 'B' (state 0, transition 2)
      - transition to state **1** (because the current state "ABCA" is rejected/incomplete)
      - current state is accepted so exit with the current string "ABCA"

## Character support

The generator does not support generating strings above the Basic Unicode plane (Plane 0). Using regexes that match characters above the basic plane may lead to unexpected behaviour.

# Tree walker types

The generator transforms each profile into one or more [decision trees](../decisionTrees/DecisionTrees.md), each of these can then be process through some strategy. The strategies for processing these trees are known as Tree walker types, each must implement `DecisionTreeWalker`.

The following walker strategies exist:

* Cartesian product (default)
* Reductive

## Cartesian product
This strategy is the a recursive algorithm which will 'multiply' each leaf node of the tree against every other leaf node of the decision tree in order to generate data. As such it can create vast numbers of permutations. This strategy makes no attempt to overcome the chance of a combinatorial explosion which can occur with relatively few rules and constraints.

This strategy uses certain methods of the Java Streams API which are known to block rather than be lazy (`flatMap`) which means the data may be prevented from being emitted until all permutations have been calculated.

This strategy is also known to have limited-to-no intelligence when it comes to [contradictions](../../user/Contradictions.md). The strategy will back track when they are found, but makes no attempt to preemptively check for them or prevent the walker from entering a contradictory path of the tree.

## Reductive (default)
This strategy takes a different approach to the others above and follows the following process. The strategy focuses on reducing the size of the problem (the tree) progressively until it cannot be any further (then back-tracking occurs) or sufficient information is known (then row/s can be emitted.) 

See [Reductive tree walker](ReductiveTreeWalker.md) for more details.

# Decision Tree Optimiser

We optimise the decision tree to improve the performance of how the generator works. 
The optimisation (also known as factorising) can reduce the number of constraints that need to be visited in-order to produce interpretations of data.

## Strategy

The process follows the steps below (exit early if there is nothing to process):

* From a given `ConstraintNode`, get all the atomic constraints in all _options_ of __decisions immediately below itself__
* Group the atomic constraints to identify if there are any which are prolific, i.e. where the atomic constraint is repeated.
* Order the groups of atomic constraints so that _NOT_ constraints are disfavoured
* Pick the first group in the ordered set, this contains an identification of the _most prolific constraint_
* Create a new `DecisionNode` and attach it to the current _root_ `ConstraintNode`
* Create a new `ConstraintNode` (_A_) under the new `DecisionNode` with the most prolific constraint as the single atomic constraint
* Create a new `ConstraintNode` (_B_) under the new `DecisionNode` with the negated form of the most prolific constraint as the single atomic constraint
* Iterate through all decisions under the root `ConstraintNode`, for each decision inspect each option (`ConstraintNode`s)
* If the option has an atomic constraint that matches the most prolific constraint, clone the option (`ConstraintNode`) [comment 2] _excluding the most prolific constraint_, add it to the new constraint (_A_)
* If the option has an atomic constraint that matches the negated form of the most prolific constraint, clone the option (`ConstraintNode`) [comment 2] _excluding the negated form of the most prolific constraint_, add it to the new constraint (_B_)
* If the option does NOT have any atomic constraint that matches the most prolific constraint (or its negated form), add the option as another option under the new `DecisionNode`
* __Simplification:__ Inspect the decisions under the most prolific constraint node (_A_), if any has a single option, then hoist the atomic constraint up to the parent constraint node. Repeat the process for the negated constraint node (_B_)
* __Recursion:__ Start the process again for both of the newly created constraint nodes (to recurse through the tree)

[comment 1]
The process will address negated (_NOT_) atomic constraints if they are the most prolific. The process will simply prefer to optimise a NON-negated constraint over that of a negated constraint where they have the same usage count.

[comment 2]
`ConstraintNodes` can be __cloned, excluding a given atomic constraint__, when this happens all other properties are copied across to the new instance, therefore all of the decisions on the constraint are preserved.

[comment 3]
The optimiser is used when generating data and when visualising the tree. It makes no attempt to maintain the order of the tree when optimising it as it isn't important for the process of data generation.

## Processing

The optimisation process will repeat the optimisation process at the current level for a maximum of __50__ (_default_) times. The process will prevent a repeat if there were no optimisations made in the previous iteration. 

The process will recurse through the tree each time it makes an optimisation, it will process any constraint node that meets the following criteria:
* The constraint has at least __2__ decisions
* The current depth is less than __10m__ (_default_)

## Testing

The optimisation process has integration tests that are run as part of every build, they take a given profile as an input and assert that the factorised tree (regardless of ordering) matches what is expected.
# Deliberate violation

## Invocation

Violation is invoked using the `generateTestCases` sub-command. An output directory must be specified rather than a single file.

## Algorithm

1. For each rule `R`:
	1. Create a new version of the profile where `R` is wrapped in a `ViolationConstraint`. A violation constraint works similar to a not constraint except that `and` conditionals are treated differently (see below).
	1. Create a decision tree from that profile, and pass it to the generator as normal.
	1. Write the output to a file in the output directory with a numerical file name.
1. Output a valid version with no rules violated.
1. Output a manifest file, listing which output file corresponts to which rule.

## Manifest

An example of a manifest file when violating one rule. 001.csv is the non-violated version, 002.csv is where the rule has been violated.
```javascript
[
  {
    "filepath": "001.csv",
    "violatedRules": []
  },
  {
    "filepath": "002.csv",
    "violatedRules": [ "Price field should not accept nulls" ]
  }
]
```

## ViolationConstraint

The `violation` constraint is an internal constraint and cannot be used specifically, only for violation. It works exactly like a `not` constraint, except where dealing with `and` constraints.

* A `not` constraint converts `¬AND(X, Y, Z)` into `OR(¬X, ¬Y, ¬Z)`
* A `violate` constraint converts `VIOLATE(AND(X, Y, Z))` into:
```
OR(
 AND(VIOLATE(X), Y, Z),
 AND(X, VIOLATE(Y), Z),
 AND(X, Y, VIOLATE(Z)))
```

This is so that we end up with each inner constraint violated separately.

## Inter-rule conflict

Sometimes when a rule is violated it may conflict with another non-violated rule. For example:

* R1: `X in (1, 2, 3)`
* R2: `X > 0`
* R3: `X < 4`

If we violate `R1` here, then we end up generating no data.

There are several approaches to this problem:
1. Delete all rules other than one being violated
2. Delete all rules that share fields with one being violated
3. Delete all constraints from other rules that affect fields affected by violated rule
4. Delete rules until the violated rule can generate
5. Negate rules until the violated rule can generate
6. Hard merge (see below)

4 and 5 could delete/negate *parts of* rules, but there's some appeal to keeping rules indivisible from a UX perspective

4 and 5 wouldn't guarantee a *full* range of results; it might be necessary to delete/negate more than one rule (see example labelled #foo, where removing only one of R2 and R3 allows incomplete output)

As of 01/11/18 we implement none of the above yet, and violating `R1` above will generate no data.

### Hard merging

Option 6 above has been discussed but is not considered the correct way to go. It involves a much lower level change in the `FieldSpecMerger` where any conflicts between a violated fieldspec and a non-violated fieldspec are resolved by simply ignoring the non-violated fieldspec instead of throwing the entire rowspec away.

Hard merging solves some cases but in the example above it struggles because the current merging logic doesn't identify it as unsatisfiable. Also it confounds user expectations by violating *other* rules without declaring it in manifest.

# Deliberate violation

_This is an alpha feature. Please do not rely on it. If you find issues with it, please [report them](https://github.com/finos/datahelix/issues)._ 

## Invocation

Violation is invoked using the `violate` command. An output directory must be specified rather than a single file.

## Algorithm

1. For each rule `R`:
	1. Create a new version of the profile where `R` is wrapped in a `ViolationConstraint`. A violation constraint works similar to a not constraint except that `and` conditionals are treated differently (see below).
	1. Create a decision tree from that profile, and pass it to the generator as normal.
	1. Write the output to a file in the output directory with a numerical file name.
1. Output a valid version with no rules violated.
1. Output a manifest file, listing which output file corresponds to which rule.

## Manifest

An example of a manifest file when violating one rule. "001.csv" is where the first rule has been violated.
```javascript
[
  {
    "filepath": "001.csv",
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

## Known issues
1. The process would be expected to return vast quantities of data, as the single constraint `foo inSet [a, b, c]` when violated returns all data except [a, b, c] from the universal set. Whilst logically correct, could result in a unusable tool/data-set due to its time to create, or eventual size.
1. The process of violating constraints also violates the type for fields, e.g. `foo ofType string` will be negated to `not(foo ofType string)`. This itself could be useful for the user to test, but could also render the data unusable (e.g. if the consumer requires the 'schema' to be adhered to)
1. The process of violating constraints also violates the nullability for fields, e.g. `foo not(is null)` will be negated to `foo is null`. This itself could be useful for the user to test, but could render the data unusable (e.g. if the consumer requires non-null values for field `foo`).
1. Implied/default rules are not negated, therefore as every field is implied/defaulted to allowing nulls, the method of violation currently doesn't prevent null from being emitted when violating. This means that nulls can appear in both normal data generation mode AND violating data generation mode.
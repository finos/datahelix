# Contradictions

Contradictions are where constraints are combined in some fashion, creating a representation of data where no, or incorrectly reduced, amounts of data can be produced. The following categories of contradictions can occur in a profile:

This document describes [how data is generated](SetRestrictionAndGeneration.md) and underpins the concept of contradictions.

| Type | Explanation | Example |
| ---- | ---- | ---- |
| 'Hard' | _cannot_ create any rows of data (for the field, and therefore the output file) | `foo ofType string` and `foo ofType decimal` and `foo not(is null)` - no rows would be emitted |
| 'Soft' | _could_ create some data, but some scenarios would produce none | `foo not null` and `if bar equalTo 1 then foo is null`, also `foo ofType string` and `foo ofType decimal`, 1 row would be emitted, containing `null` |

There is an optional component of the generator - the profile validator - which can detect some of the above contradictions. See the explanation of each type for more detail.

## 'Hard' contradictions
This is where contradictions occur in a way where no rows could be satisfied for at least one field. If this is the case then no rows can be emitted for any field in the profile, therefore the output file/s would be empty.
Hard contradictions can otherwise be described as removing all possible values from the universal set and denying the absence of values for the field; `not(is null)`. All hard contradictions must contain this constraint in some fashion, otherwise the field can still have no value (regularly represented as `null`).

See [how data is generated](SetRestrictionAndGeneration.md) for more detail on how constraints are combined and in-turn reduce the set of permissible values for a field.

Examples are:
* `is null` and `not(is null)`
* `ofType string` and `ofType decimal` and `not(is null)`
* `ofType string` and `shorterThan 1` and `not(is null)`

The contradictions that the validator will detect are [documented here](../../generator/docs/ProfileValidation.md).

Examples of profiles are:
* [Null Validation](../../examples/hard-contradiction-null-validation/profile.json)
* [Type Validation 1](../../examples/hard-contradiction-type-validation-1/profile.json)
* [Type Validation 2](../../examples/hard-contradiction-type-validation-2/profile.json)

## 'Soft' contradictions
This is where contradictions only appear in more complex constraints, e.g. `anyOf`, `if`, `allOf`, etc. It can also be related to where different types of constraints are combined, e.g. `aValid ISIN` and `matchingRegex /[a-z]{10}/`.

These contradictions are more difficult to detect, and in some cases are only apparent when generating data.

The contradictions that the validator will detect are [documented here](ProfileValidation.md).

Examples of profiles are:
* [Type Validation](../../examples/soft-contradictions/profile.json)

## Non-contradictory examples
The following are examples of where constraints can be combined and (whilst potentially dubious) are not contradictory:
* `foo inSet ["a", "b", 1, 2]` and `foo greaterThan 1`
  * this can emit `"a", "b", 2` or nothing (`null`) as there is nothing to say it must have a value, or must be of a particular type
* `foo greaterThan 1` and `foo ofType string`
  * this can emit all strings, or emit no value (`null`) (the `greaterThan` constraint is ignored as it only applies to `decimal` or `integer` values, of which none will be generated)

## What happens
1. 'hard' contradictions only abort processing when the validator is enabled
1. 'soft' contradictions never abort processing and can prevent rows from being emitted (correctly or not)
1. Cucumber tests do not assert whether 'hard' or 'soft' contradictions have been detected (nor do they attempt to check)
1. The profile validator is an optional component that has to be opted-in to use
1. The generator will try to generate data whether contradictions are present or not, it may only emit no rows if the combinations are fully-contradictory

## Current stance
1. The cucumber framework of tests should be able to (optionally) detect 'hard' contradictions - [#202](https://github.com/ScottLogic/data-engineering-generator/issues/202)
1. The profile validator should be defaulted to "on for errors" but have the option for disabling it - [#467](https://github.com/ScottLogic/data-engineering-generator/issues/467)
1. Contradiction checking to a greater degree will be deferred to tooling for the generator, such as profile writers, user interfaces, etc.

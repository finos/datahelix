# Contradictions

Contradictions are where constraints are combined in some fashion, creating a representation of data where no, or incorrectly reduced, amounts of data can be produced.
The following categories of contradictions can occur in a profile:


| Type | Explanation | Example |
| ---- | ---- | ---- |
| 'Hard' | _cannot_ create any rows of data (for the field, and therefore the output file) | `foo ofType string` and `foo ofType decimal` and `foo not(is null)` - no rows would be emitted |
| 'Partial' | _could_ create some data, but some scenarios would produce none | `foo equalTo 1` and `anyOf foo equalTo 1 or foo equalTo 2` (1 is produced), also `foo ofType string` and `foo ofType decimal` (`null` is produced) |

Note that the phases "there is a hard contradiction", "the profile is fully contradictory" and "the profile is wholly contradictory" are synonymous.

This document describes [how data is generated](SetRestrictionAndGeneration.md) and underpins the concept of contradictions.

## 'Hard' contradictions
This is where contradictions occur in a way where no rows could be satisfied for at least one field. If this is the case then no rows can be emitted for any field in the profile, therefore the output file/s would be empty.
Hard contradictions can otherwise be described as removing all possible values from the universal set and denying the absence of values for the field; `not(is null)`.

Note that these contradictions can occur even if most of the profile is correct. If no data can be generated for a single field,
then it will prevent all the other fields from producing data.

See [how data is generated](SetRestrictionAndGeneration.md) for more detail on how constraints are combined and in-turn reduce the set of permissible values for a field.

Examples are:
* `is null` and `not(is null)`
* `ofType string` and `ofType decimal` and `not(is null)`
* `ofType string` and `shorterThan 1` and `not(is null)`
* `equalTo 1` and `equalTo 2`

The generator can detect some contradictions upfront and will report them, but for performance reasons cannot detect all types.
If no data is generated for a file, this means that the profile has a hard contradiction. 

Examples of profiles are:
* [Null Validation](../../examples/hard-contradiction-null-validation/profile.json)
* [Type Validation 1](../../examples/hard-contradiction-type-validation-1/profile.json)
* [Type Validation 2](../../examples/hard-contradiction-type-validation-2/profile.json)

## 'Partial' contradictions
This is where part of a tree is fully contradictory.

Examples of profiles are:
* [Partial Contradiction in anyOf](../../examples/partial-contradictions/profile.json)

## Non-contradictory examples
The following are examples of where constraints can be combined and (whilst potentially dubious) are not contradictory:
* `foo inSet ["a", "b", 1, 2]` and `foo greaterThan 1`
  * this can emit `"a", "b", 2` or nothing (`null`) as there is nothing to say it must have a value, or must be of a particular type
* `foo greaterThan 1` and `foo ofType string`
  * this can emit all strings, or emit no value (`null`) (the `greaterThan` constraint is ignored as it only applies to `decimal` or `integer` values, of which none will be generated)

## What happens
1. Detected 'hard' contradictions give an error and produce no data.
1. Detected 'partial' contradictions give an error but produce some data.
1. The generator will try to generate data whether contradictions are present or not, it may only emit no rows if the combinations are fully contradictory.

## Current stance
1. Contradiction checking to a greater degree will be deferred to tooling for the generator, such as profile writers, user interfaces, etc.
1. More detailed contradiction checking (#1090) and better upfront warnings (#896) are being considered as features.

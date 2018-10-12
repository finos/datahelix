# Profiles

**Data profiles** describe potential or real data. For instance, we could design a profile that describes a _user_account_ table, where:

* the data must have _user_id_, _email_address_ and _creation_date_ fields
* _user_id_ is a non-optional string
* _email_address_ must be populated and contain a @ character
  * however, if _user_id_ is itself an email address, _email_address_ must be absent
* _creation_date_ is a non-optional date, with no time component, later than 2003 and output per [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601)    

Every profile declares some **fields** and some **constraints**.

## Fields

Fields are the "slots" of data that can take values. If generating into a CSV or database, fields are columns. If generating into JSON, fields are object properties. Typical fields might be _email_address_ or _user_id_.

By default, any piece of data is valid for a field. To get more specific, we need to add **rules**.

## Rules

Every rule is a named collection of *constraints*, which can be any of:

* [Epistemic constraints](EpistemicConstraints.md), which limit a field's possible values
* [Presentational constraints](PresentationalConstraints.md), which control how values are serialised (eg, number of significant figures)
* [Grammatical constraints](GrammaticalConstraints.md), which combine other constraints together

(Epistemic and formatting constraints are collectively referred to as **data constraints**)

The decision of how to group constraints into rules is up to the user. At the extremes, there could be a separate rule for each constraint, or one rule containing every constraint. More usually, rules will represent collections of related constraints (eg, _"X is a non-null integer between 0 and 100"_ is a fine rule, comprising four constraints). How to group into rules becomes particularly important when [deliberate violation](../generator/docs/DeliberateViolation.md) is involved.

## Persistence

Profiles are persisted as JSON documents following a [schema](Schema.md).

## Creation

Profiles can be created by any of:

- Writing JSON profiles by hand or by some custom transform process
- ~~Deriving them by supplying some sample data to the Profiler~~ (not yet)
- ~~Designing them through the web front end~~ (not yet)


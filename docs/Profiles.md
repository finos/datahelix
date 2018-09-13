# Profiles

**Data profiles** describe potential or real data. For instance, we could design a profile that describes a _user_account_ table, where:

* the data must have _user_id_, _email_address_ and _creation_date_ fields
* _user_id_ is a non-optional string
* _email_address_ must be populated and contain a @ character
  * however, if _user_id_ is itself an email address, _email_address_ must be absent
* _creation_date_ is a non-optional date, with no time component, later than 2003 and output per [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601)    

We can model these and other requirements through combinations of **constraints**, which make assertions about what properties a valid data entry must have or how it must be formatted. Constraints are either [grammatical](GrammaticalConstraints.md) or [data](DataConstraints.md) constraints.

## Persistence

Profiles are persisted as JSON documents following a [schema](Schema.md).

## Creation

Profiles can be created by any of:

- Writing JSON profiles by hand or by some custom transform process
- Deriving them by supplying some sample data to the Profiler
- Designing them through the web front end


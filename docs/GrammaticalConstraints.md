# Grammatical constraints

**Grammatical constraints** combine or modify other constraints. They are fully recursive; any grammatical constraint is a valid input to any other grammatical constraint.

See [set restriction and generation](./../generator/docs/SetRestrictionAndGeneration.md) for an indepth explanation of how the constraints are merged and data generated from them.

## `not`

```javascript
{ "not": { "field": "foo", "is": "null" } }
```

Wraps a constraint. Is satisfied if, and only if, its inner constraint is _not_ satisfied.

## `anyOf`

```javascript
{ "anyOf": [
    { "field": "foo", "is": "null" },
    { "field": "foo", "is": "equalTo", "value": 0 }
]}
```

Contains a number of sub-constraints. Is satisfied if any of the inner constraints are satisfied.

## `allOf`

```javascript
{ "allOf": [
    { "field": "foo", "is": "ofType", "value": "integer" },
    { "field": "foo", "is": "equalTo", "value": 0 }
]}
```

Contains a number of sub-constraints. Is satisfied if all of the inner constraints are satisfied.

## `if`

```javascript
{
    "if":   { "field": "foo", "is": "ofType", "value": "integer" },
    "then": { "field": "foo", "is": "greaterThan", "value": 0 },
    "else": { "field": "foo", "is": "equalTo", "value": "N/A" }
}
```

Is satisfied if either:
 
- Both the `if` and `then` constraints are satisfied
- The `if` constraint is not satisfied, and the `else` constraint is

While it's not prohibited, wrapping conditional constraints in any other kind of constraint (eg, a `not`) may cause unintuitive results.
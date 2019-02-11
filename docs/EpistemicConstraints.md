# Epistemic constraints

## Theory

* An **epistemic constraint** is a predicate that defines any given value as being _valid_ or _invalid_
* The **universal set** contains all generatable values (`null`, any string, any date, any number, etc)
* The **denotation** of a constraint is the subset of the universal set that it defines as valid

See [set restriction and generation](/generator/docs/SetRestrictionAndGeneration.md) for an indepth explanation of how the constraints are merged and data generated from them.

If no constraints are defined over a field, then it can accept any member of the universal set. Each constraint added to that field progressively limits the universal set.

The [grammatical `not` constraint](GrammaticalConstraints.md) inverts a constraint's denotation; in other words, it produces the complement of the constraint's denotation and the universal set.

## General constraints

### `equalTo` _(field, value)_

```javascript
{ "field": "type", "is": "equalTo", "value": "X_092" }
OR
{ "field": "type", "is": "equalTo", "value": 23 }
OR
{ "field": "type", "is": "equalTo", "value": { "date": "2001-02-03T04:05:06.007" } }
```

Is satisfied if `field`'s value is equal to `value`

### `inSet` _(field, values)_

```javascript
{ "field": "type", "is": "inSet", "values": [ "X_092", 123, null, { "date": "2001-02-03T04:05:06.007" } ] }
```

Is satisfied if `field`'s value is in the set `values`

### `null` _(field)_

```javascript
{ "field": "price", "is": "null" }
```

Is satisfied if `field` is null or absent.

### `ofType` _(field, value)_

```javascript
{ "field": "price", "is": "ofType", "value": "string" }
```

Is satisfied if `field` is of type represented by `value` (valid options: `numeric`, `string`, `temporal`)

## Textual constraints
These constraints imply `ofType string`.

### `matchingRegex` _(field, value)_

```javascript
{ "field": "name", "is": "matchingRegex", "value": "[a-z]{0, 10}" }
```

Is satisfied if `field` is a string matching the regular expression expressed in `value`. The regular expression must match the entire string in `field`, start and end anchors `^` & `$` are ignored.

### `containingRegex` _(field, value)_

```javascript
{ "field": "name", "is": "containingRegex", "value": "[a-z]{0, 10}" }
```

Is satisfied if `field` is a string containing the regular expression expressed in `value`. Using both start and end anchors `^` & `$` make the constraint behave like `matchingRegex`.

### `ofLength` _(field, value)_

```javascript
{ "field": "name", "is": "ofLength", "value": 5 }
```

Is satisfied if `field` is a string whose length exactly matches `value`, must be a whole number.
    
### `longerThan` _(field, value)_

```javascript
{ "field": "name", "is": "longerThan", "value": 3 }
```

Is satisfied if `field` is a string with length greater than `value`, must be a whole number.

### `shorterThan` _(field, value)_

```javascript
{ "field": "name", "is": "shorterThan", "value": 3 }
```

Is satisfied if `field` is a string with length less than `value`, must be a whole number.   

### `aValid` _(field, value)_

```javascript
{ "field": "name", "is": "aValid", "value": "ISIN" }
```

Is satisfied if `field` is a valid `value`, in this case a valid ISIN code. Possible options for `value` are:
* ISIN

**NOTE**: This constraint cannot be combined with any other textual constraint, doing so will mean no string data is created. See [Frequently asked questions](FrequentlyAskedQuestions.md) for more detail.

## Numeric constraints
These constraints imply `ofType numeric`.

### `greaterThan` _(field, value)_

```javascript
{ "field": "price", "is": "greaterThan", "value": 0 }
```

Is satisfied if `field` is a number greater than `value`.

### `greaterThanOrEqualTo` _(field, value)_

```javascript
{ "field": "price", "is": "greaterThanOrEqualTo", "value": 0 }
```

Is satisfied if `field` is a number greater than or equal to `value`.

### `lessThan` _(field, value)_

```javascript
{ "field": "price", "is": "lessThan", "value": 0 }
```

Is satisfied if `field` is a number less than `value`.

### `lessThanOrEqualTo` _(field, value)_

```javascript
{ "field": "price", "is": "lessThanOrEqualTo", "value": 0 }
```

Is satisfied if `field` is a number less than or equal to `value`.

### `granularTo` _(field, value)_

```javascript
{ "field": "price", "is": "granularTo", "value": 0.1 }
```

Is satisfied if `field` has the granularity specified in `value`. Numerical granularities must be numbers, either 1 or a fractional power of ten; valid values can have no more meaningful decimal places than the granularity. Example values:

- `1` would permit only integers
- `0.1` would permit `20` and `20.1`, but not `20.01` 

## Temporal constraints
These constraints imply `ofType temporal`. All dates must be in format `yyyy-MM-ddTHH:mm:ss.SSS` and embedded in a _date-object_.

Example: `{ "date": "2001-02-03T04:05:06.007" }`

### `after` _(field, value)_

```javascript
{ "field": "date", "is": "after", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring after `value`.

### `afterOrAt` _(field, value)_

```javascript
{ "field": "date", "is": "afterOrAt", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring after or simultaneously with `value`.

### `before` _(field, value)_

```javascript
{ "field": "date", "is": "before", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring before `value`.

### `beforeOrAt` _(field, value)_

```javascript
{ "field": "date", "is": "beforeOrAt", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring before or simultaneously with `value`.

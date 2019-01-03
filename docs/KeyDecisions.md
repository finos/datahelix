# Key decisions

## Do type-specific constraints imply corresponding type constraints?

For instance, what is generated for the price field in this example?

```javascript
{ "field": "price", "is": "greaterThan", "value": 4 }
```

- Option 1: Only numbers greater than 4 (and null)
- Option 2: Everything except numbers less than or equal to 4 (eg, strings are valid). Users are expected to supplement with type constraints

## Does negating a constraint complement its denotation?

In other words, given a constraint `C`, is `¬C` satisfied by everything that doesn't satisfy `C`?

In some cases this is intuitive:

- If `C` says that a field is null, `¬C` should permit that field to be anything _other_ than null.
- If `C` says that a field is in a set, `¬C` should permit anything _not_ in that set.
- If `C` says that a field is numeric, `¬C` should permit strings, temporal values, etc.

But:

- If `C` says that a field is a number greater than 3, it might be intuitive to say that `¬C` permits numbers less than or equal to 3.     

## Does an inSet constraint imply anything about nullability?

```javascript
{ "field": "product_type", "is": "inSet", "values": [ "a", "b" ] }
```

Given the above, should we expect nulls? If null is considered a _value_ then no would be a reasonable answer, but it can equally be considered the absence of a value. 

## What do temporal constraints mean when datetimes are partially specified?

```javascript
{ "field": "creationDate", "is": "after", "value": "2015-01-01" }
```

Should I be able to express the above, and if so what does it mean? Intuitive, we might say that it cannot be satisfied with, eg, `2015-01-01 12:00:00`, but it depends on how we interpret underspecified datetimes:

* As an **instant**? If so, `2015-01-01` is interpreted as `2015-01-01 00:00:00.000`, and even a datetime 0.01 milliseconds later would satisfy the above constraint.
* As a **range**? If so, `2015-01-01` is interpreted as `2015-01-01 00:00:00.000 ≤ X < 2015-01-02 00:00:00.000`, and the `before` and `after` constraints are interpreted relative to the start and end of this range, respectively.

Both of these approaches seem more or less intuitive in different cases (for example, how should `equalTo` constraints be applied?). To resolve this problem, we currently require datetime expressions to be fully specified down to thousandths of milliseconds.

## How should we generate characters above the Basic Unicode Plane?

We currently only support generation of characters represented in range 0000-FFFF.

Either we can:
1) Update the tool to reject any regular expressions that contain characters outside of this range.
2) Update the tool to accept & generate these characters
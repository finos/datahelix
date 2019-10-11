# Key decisions

## Do type-specific constraints imply corresponding type constraints?

For instance, what is generated for the price field in this example?

```javascript
{ "field": "price", "is": "greaterThan", "value": 4 }
```

This constraint means :
  * Everything except numbers less than or equal to 4 (eg, strings are valid). Users are expected to supplement with type constraints


## Does negating a constraint complement its denotation?

In other words, given a constraint `C`, is `¬C` satisfied by everything that doesn't satisfy `C`?

In some cases this is intuitive:

- If `C` says that a field is null, `¬C` should permit that field to be anything _other_ than null.
- If `C` says that a field is in a set, `¬C` should permit anything _not_ in that set.
- If `C` says that a field is a decimal, `¬C` should permit strings, datetimes, etc.

But:

- If `C` says that a field is a number greater than 3, it might be intuitive to say that `¬C` permits numbers less than or equal to 3.     

Note that negation of type integer is not fully defined yet as we do not have a negation of granularTo implemented.

## Does an inSet constraint imply anything about nullability?

```javascript
{ "field": "product_type", "inSet": [ "a", "b" ] }
```

Given the above, should we expect nulls? If null is considered a _value_ then no would be a reasonable answer, but it can equally be considered the absence of a value. 

## What do datetime constraints mean when datetimes are partially specified?

```javascript
{ "field": "creationDate", "is": "after", "value": "2015-01-01" }
```

Should I be able to express the above, and if so what does it mean? Intuitive, we might say that it cannot be satisfied with, eg, `2015-01-01 12:00:00`, but it depends on how we interpret underspecified datetimes:

* As an **instant**? If so, `2015-01-01` is interpreted as `2015-01-01 00:00:00.000`, and even a datetime 0.01 milliseconds later would satisfy the above constraint.
* As a **range**? If so, `2015-01-01` is interpreted as `2015-01-01 00:00:00.000 ≤ X < 2015-01-02 00:00:00.000`, and the `before` and `after` constraints are interpreted relative to the start and end of this range, respectively.

Both of these approaches seem more or less intuitive in different cases (for example, how should `equalTo` constraints be applied?). To resolve this problem, we currently require datetime expressions to be fully specified down to thousandths of milliseconds.

## How should we generate characters outside the basic latin character set?

We currently only support generation of characters represented in range 002c-007e.

Either we can:
1) Update the tool to reject any regular expressions that contain characters outside of this range.
2) Update the tool to accept & generate these characters

## Is the order of rows emitted between each run consistent?

We currently do not guarantee that the order of valid rows is consistent between each generation run. For example on one execution we may produce the following output for three fields:

| Field A | Field B | Field C |
|---------|---------|---------|
| 1       | Z       | 100.5   |
| 1       | Z       | 95.2    |
| 2       | Z       | 100.5   |
| 2       | Z       | 95.2    |
| 1       | Y       | 100.5   |
| 1       | Y       | 95.2    |

However on another using the same profile we may produce the following output:

| Field A | Field B | Field C |
|---------|---------|---------|
| 1       | Z       | 100.5   |
| 1       | Z       | 95.2    |
| 1       | Y       | 100.5   |
| 1       | Y       | 95.2    |
| 2       | Z       | 100.5   |
| 2       | Z       | 95.2    |

Both produce valid output and both produce the same values as a whole but in slightly different order.
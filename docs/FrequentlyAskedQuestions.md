# Frequently asked questions

## "What's the difference between formattedAs and granularTo?"

The below constraints are superficially similar:

```javascript
{ "field": "price", "is": "granularTo", "value": 0.01 }
{ "field": "price", "is": "formattedAs", "value": "%.2f" }
```

The difference is that `granularTo` affects which numbers are generated, while `formattedAs` affects how they are converted to strings. In fact, if the data is output in some format that doesn't require numbers to be converted to strings (eg, CSV does but JSON does not) then `formattedAs` will have no effect whatsoever on a numeric value.

Consider this example:

```javascript
{ "field": "price", "is": "granularTo", "value": 0.01 },
{ "not": { "field": "price", "is": "null" } },
{ "field": "price", "is": "greaterThanOrEqualTo", "value": 0 },
{ "field": "price", "is": "lessThanOrEqualTo", "value": 1 },
```

This combination of constraints will generate values `[ 0, 0.01, 0.02, 0.03, ..., 1 ]`. If outputted to a CSV file, they would be printed exactly as in that list. If this presentational constraint were added:

```javascript
{ "field": "price", "is": "formattedAs", "value": "%.1f" }
```

then our CSV might look like this instead:
 
```csv
price
0.0
0.0
0.0
0.0
[...]
1.0
```

while the same data output to JSON would retain the original full precision:

```javascript
[
    { "price": 0 },
    { "price": 0.01 },
    { "price": 0.02 },
    { "price": 0.03 },
    ...
    { "price": 1 },
]
```

To reiterate, `formattedAs` only affects how data is presented _after_ it has been generated. It has no impact on _what_ data gets generated, and can be ignored entirely for many data types and output formats. 

## Do `inSet` or `equalTo` permit or deny the empty set (&#8709;)?
In other words, do `inSet` or `equalTo` prevent `null` from being emitted?

In short, **no**, `null` can still be emitted.

The `inSet` operator only defines the initial set of data to work from, but does not convey any instruction or definition that the empty set (&#8709;) is not permitted.

The `equalTo` operator is short-hand for `inSet`, as such abides by the same rules.

Both of the above operators will explicitly deny the inclusion of `null`, therefore `equalTo null` and `inSet [null]` (or any set that contains `null`) will throw an error and abort processing.

All fields permit the inclusion of the empty set (&#8709;) by default, to prevent the field from having a `null` emitted, ensure you use the `not(is null)` constraint.

For more details see the [set restriction and generation](./../generator/docs/SetRestrictionAndGeneration.md) page.

# Presentational Constraints

### `formattedAs` _(field, value)_

```javascript
{ "field": "price", "is": "formattedAs", "value": "%.5s" }
```

Used by output serialisers where string output is required. `value` must be:

* a string recognised by Java's `String.format` method
* appropriate for the data type of `field`
* not `null` (formatting will not be applied for null values)

See the [FAQ](FrequentlyAskedQuestions.md) for the difference between this and [granularTo](EpistemicConstraints.md).

# Presentational constraints

### `formattedAs` _(field, value)_

```javascript
{ "field": "price", "is": formattedAs", "value": "%.5s" }
```

Used by output serialisers where string output is required. `value` must be:

* a string recognised by Java's `String.format` method
* appropriate for the data type of `field`

See the [FAQ](FrequentlyAskedQuestions.md#"what's-the-difference-between-formattedas-and-granularto?") for the difference between this and [granularTo](EpistemicConstraints.md#granularTo).

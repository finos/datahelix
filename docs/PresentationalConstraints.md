# Presentational constraints

### `formattedAs` _(field, value)_

```javascript
{ "field": "price", "formattedAs": "%.5s" }
```

Used by output serialisers where string output is required. `value` must be:

* a string recognised by Java's `String.format` method
* appropriate for the data type of `field`

# Profile schema

## Sample file
```javascript
{
	"schemaVersion": "0.16",
	"description": "A dataset about financial products",
	"fields":
	[
		{
			"name": "id",
			"type": "string",
			"nullable": false
		},
		{
			"name": "time",
			"type": "datetime"
		},
		{
			"name": "country",
			"type": "string"
		},
		{
			"name": "tariff",
			"type": "decimal"
		},
		{
			"name": "low_price",
			"type": "integer",
			"nullable": false
		},
		{
			"name": "high_price",
			"type": "integer"
		}
	],
    "constraints": [
				{ "field": "low_price", "greaterThanOrEqualTo": 0 },
                { "field": "country", "inSet": [ "USA", "GB", "FRANCE" ] },
                {
					"if": {
						"anyOf": [
							{ "field": "country", "equalTo": "USA" },
							{ "field": "country", "is": "null" }
						]
					},
					"then": {
						"allOf": [
							{ "field": "tariff", "is": "null" },
							{ "field": "time", "after": "2014-01-01" }
						]
					},
					"else": { "not": { "field": "tariff", "is": "null" } }
				}
	]
}
```

## Constituent objects

### `Profile`
* `"description"`: An optional description of what data the profile is modelling.
* `"fields"`: A set of one or more `Field` objects. Each field must have a unique name.
* `"constraints"`: A set of `Constraint` objects.

### `Field`

A field in the data set.

* `"name"`: The field's name. Should be unique, as constraints will reference fields by name. This property is used for, eg, column headers in CSV output
* `"type"`: The field's data type.
* `"formatting"`: The formatting used for the output of the field. (Optional)
* `"unique"`: Sets if the field is unique. (Optional)
* `"nullable"`: Sets if null is an allowed output of the field. (Optional)

### `Constraint`

One of:

- a [predicate constraint](https://github.com/finos/datahelix/blob/master/docs/UserGuide.md#Predicate-constraints)
- a [grammatical constraint](https://github.com/finos/datahelix/blob/master/docs/UserGuide.md#Grammatical-constraints)


The Profile schema format is formally documented in the [User Guide](https://github.com/finos/datahelix/blob/master/docs/UserGuide.md).

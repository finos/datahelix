# Profile schema

## Sample file
```javascript
{
	"schemaVersion": "0.1",
	"description": "A dataset about financial products",
	"fields":
	[
		{ "name": "id" },
		{ "name": "time" },
		{ "name": "country" },
		{ "name": "tariff" },
		{ "name": "low_price" },
		{ "name": "high_price" }
	],
	"rules":
	[
		{
			"rule": "id is a non-nullable string",
			"constraints":
			[
				{ "field": "id", "is": "ofType", "value": "string" },
				{ "not": { "field": "id", "is": "null" } }
			]
		},

		{
			"rule": "low_price is a non-nullable positive integer",
			"constraints": [
				{ "field": "low_price", "is": "ofType", "value": "integer" },
				{ "not": { "field": "low_price", "is": "null" } },
				{ "field": "low_price", "is": "greaterThanOrEqualTo", "value": 0 }
			]
		},
		{ 
			"rule": "allowed countries",
			"constraints": [
				{ "field": "country", "is": "inSet", "values": [ "USA", "GB", "FRANCE" ] }
			]
		},
		{
			"rule": "country tariffs",
			"constraints": [
				{
					"if": {
						"anyOf": [
							{ "field": "country", "is": "equalTo", "value": "USA" },
							{ "field": "country", "is": "null" }
						]
					},
					"then": {
						"allOf": [
							{ "field": "tariff", "is": "null" },
							{ "field": "time", "is": "after", "value": { "date": "2014-01-01" } }
						]
					},
					"else": { "not": { "field": "tariff", "is": "null" } }
				}
			]
		}
	]
}
```

## Constituent objects

### `Profile`
* `"description"`: An optional description of what data the profile is modelling.
* `"fields"`: A set of one or more `Field` objects. Each field must have a unique name.
* `"rules"`: A set of one or more `Rule` objects which must contain one or more `Constraint` objects.

### `Field`

A field in the data set.

* `"name"`: The field's name. Should be unique, as constraints will reference fields by name. This property is used for, eg, column headers in CSV output
* `"formatting"`: The formatting used for the output of the field. (Optional)
* `"unique"`: Sets if the field is unique. (Optional)
* `"nullable"`: Sets if null is an allowed output of the field. (Optional)

### `Rule`
A named collection of constraints. Test case generation revolves around rules, in that the generator will output a separate dataset for each rule, wherein each row violates the rule in a different way.

* `"rule"`: A textual description of the rule
* `"constraints"`: A set of constraints composing this rule

### `Constraint`

One of:

- a [predicate constraint](UserGuide.md#Predicate-constraints)
- a [grammatical constraint](UserGuide.md#Grammatical-constraints)
- a [presentational constraint](UserGuide.md#Presentational-constraints)


The Profile schema format is formally documented in the [User Guide](UserGuide.md).

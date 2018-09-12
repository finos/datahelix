# Profile schema

## Sample file
```javascript
{
	"schemaVersion": "v3",
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
				{ "field": "low_price", "is": "ofType", "value": "numeric" },
				{ "not": { "field": "low_price", "is": "null" } },
				{ "field": "low_price", "is": "greaterThanOrEqualTo", "value": 0 }
			]
		},

		{ "field": "country", "is": "inSet", "values": [ "USA", "GB", "FRANCE" ] },

		{
			"if": {
				"anyOf": [
					{ "field": "type", "is": "equalTo", "value": "USA" },
					{ "field": "type", "is": "null" }
				]
			},
			"then": {
				"allOf": [
					{ "field": "tariff", "is": "null" },
					{ "field": "time", "is": "greaterThan", "value": "2014-01-01" }
				]
			},
			"else": { "not": { "field": "tariff", "is": "null" } }
		}
	]
}
```

## Constituent objects

### `Profile`
* `"description"`: A description of what data the profile is modelling
* `"fields"`: A set of `Field` objects
* `"rules"`: A set of objects, each either a `Rule` object or a `Constraint` object. If a `Constraint` object is supplied, it is implicitly interpreted as a `Rule`, with a description automatically generated from the constraint itself.

### `Field`

A field in the dataset.

* `"name"`: The field's name. Should be unique, as constraints will reference fields by name. This property is used for, eg, column headers in CSV output

### `Rule`
A named collection of constraints. Test case generation revolves around rules, in that the generator will output a separate dataset for each rule, wherein each row violates the rule in a different way.

* `"rule"`: A textual description of the rule
* `"constraints"`: A set of constraints composing this rule

### `Constraint`

Either of:

- a [data constraint](DataConstraints.md)
- a [grammatical constraint](ConstraintGrammar.md)

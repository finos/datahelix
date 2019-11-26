# Interesting Generation Mode

_This is an alpha feature. Please do not rely on it. If you find issues with it, please [report them](https://github.com/finos/datahelix/issues)._

The _interesting generation mode_ exists to provide a means of generating smaller sets of data. To illustrate, consider the following profile:

```
{
    "fields": [
		{ "name": "field1" }
	],
	"constraints": [
		{
			"field": "field1",
			"is": "ofType",
			"value": "string"
		},
		{
			"field": "field1",
			"shorterThan": 5
		},
		{
			"not": {
				"field": "field2",
				"is": "null"
			}
		}	
	]
}
```

The above describes a data set where there is one field which can emit any string so long as it is shorter than 5 characters. The generator can emit the following (see [Generation strategies](https://github.com/ScottLogic/datahelix/blob/master/docs/Options/GenerateOptions.md)):

Unicode has 55,411 code-points (valid characters) in [the basic multilingual plane](https://en.wikipedia.org/wiki/Plane_(Unicode)) - that the generator will emit characters from. In the table below this number is represented as _#[U](https://en.wikipedia.org/wiki/Universal_set)_.

| mode | what would be emitted | potential number of rows |
| ---- | ---- | ---- |
| full sequential | any string that is empty, 1, 2 or 3 combinations of any unicode characters | _#U_<sup>0</sup> + _#U_<sup>1</sup> + _#U_<sup>2</sup> + _#U_<sup>3</sup> = 170,135,836,825,864 |
| random | an infinite production of random values from full sequential | unlimited |
| interesting | interesting strings that abide by the constraints | 3-4 |

Given this simple profile, using full-sequential generation you would expect to see _**170 trillion**_ rows, if the generator was unlimited in the number of rows it can emit. One of the goals of the DataHelix project is to generate data for testing systems, this amount of data for testing is complete, but in itself difficult for use due to its size.

It would be more useful for the generator to emit data that matches some data that presents normal and abnormal attributes. If a user wanted to test that another product can accept these strings you might expect the following scenarios:

* an empty string
* a string of 4 characters
* a string of 1 character
* a string of characters including non-ASCII characters - but never the less unicode characters - e.g. an :slightly_smiling_face:
* a string containing at least one [null character](https://en.wikipedia.org/wiki/Null_character)

The above generation strategy is called _interesting_ generation in the generator. The above list is indicative and designed to give a flavour of what the strategy is trying to achieve. 

The user may also want to generate data that [deliberately violates](DeliberateViolation.md) the given rules for the field, to test that the other product exhibits the expected behaviour when it is provided invalid data. If this was the case you might expect to test the system with the following data:

* no value (otherwise represented as `null`)
* a string of 5 characters
* a numeric value
* a temporal value
* a boolean value

The values that the generator will emit for various scenarios are [documented here](../generationTypes/GenerationTypes.md#interesting). Some of the scenarios above are not met, see the linked document for their details.
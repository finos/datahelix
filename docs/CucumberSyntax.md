The cucumber framework has been developed to facilitate the production of behavioural driven development and testing.
The framework uses [gherkin specs](https://docs.cucumber.io/gherkin/) which allow for 'english language' tests to be executed using the [cucumber](https://docs.cucumber.io/) framework.

The cucumber framework has been integrated into the generator to support the [[Testing strategy]].

### Example
```
Feature: the name of my feature

Background:
     Given the generation strategy is interesting
       And there is a field foo

  Scenario: Running the generator should emit the correct data
     Given foo is equal to 8
     Then the following data should be generated:
       | foo  |
       | 8    |
       | null |
```

More examples can be seen in the [generator cucumber features](https://github.com/ScottLogic/datahelix/tree/master/generator/src/test/java/com/scottlogic/deg/generator/cucumber)

We wont go into details on [gherkin specs](https://docs.cucumber.io/gherkin/) in this document, please use the gherkin documentation to understand the syntax.

The framework supports setting configuration settings for the generator, defining the profile and describing the expected outcome. All of these are described below, all variable elements (e.g. `{generationStrategy}` are case insensitive), all fields and values **are case sensitive**.

### Configuration options
* _the generation strategy is `{generationStrategy}`_ see [generation strategies](https://github.com/ScottLogic/datahelix/blob/master/docs/Options/GenerateOptions.md) - default: `full`
* _the combination strategy is `{combinationStrategy}`_ see [combination strategies](https://github.com/ScottLogic/datahelix/blob/master/docs/Options/GenerateOptions.md) - default: `pinning`
* _the walker type is `{walkerType}`_ see [walker types](https://github.com/ScottLogic/datahelix/blob/master/docs/Options/GenerateOptions.md) - default: `cartesian_product`
* _the data requested is `{generationMode}`_, either `violating` or `validating` - default: `validating`
* _the generator can generate at most `{int}` rows_, ensures that the generator will only emit `int` rows, default: `10000000` (10 million)
* _we do not violate constraint `{operator}`_, prevent this operator from being violated (see **Operators** section below), you can specify this step many times if required

### Defining the profile
It is important to remember that constraints are built up of 3 components: a field, an operator and most commonly an operand. In the following example the operator is 'greaterThan' and the operand is 5.

```
foo is greaterThan 5
```

Operators are converted to English language equivalents for use in cucumber, so 'greaterThan' is expressed as 'greater than'.

* _there is a field `{field}`_, adds a field called `field` to the profile
* _the following fields exist:_, adds a set of fields to the profile (is followed by a single column set of strings, each represents a field name)
* _`{field}` is null_, adds a null constraint to the profile for the field `field`
* _`{field}` is anything but null_, adds a not(is null) constraint to the profile for field `field`
* _`{field}` is `{operator}` `{operand}`_, adds an `operator` constraint to the field `field` with the data `operand`, see **operators** and **operands** sections below
* _`{field}` is anything but `{operator}` `{operand}`_, adds a negated `operator` constraint to the field `field` with the data `operand`, see **operators** and **operands** sections below
* _there is a constraint:_, adds the given JSON block as a constraint as if it was read from the profile file itself. It should only be used where the constraint cannot otherwise be expressed, e.g. for `anyOf`, `allOf` and `if`.

#### Operators
See [Epistemic Constraints](https://github.com/ScottLogic/datahelix/blob/master/docs/EpistemicConstraints.md), [Grammatical Constraints](https://github.com/ScottLogic/datahelix/blob/master/docs/GrammaticalConstraints.md) and [Presentational Constraints](https://github.com/ScottLogic/datahelix/blob/master/docs/PresentationalConstraints.md) documents for details of the constraints.

#### Operands
When specifying the operator/s for a field, ensure to format the value as in the table below:

| data type | example |
| ---- | ---- |
| string | "my value" |
| number | `1.234` |
| datetime | `2001-02-03T04:05:06.000` |
| null | `null` |

datetime values must be expressed as above (i.e. `yyyy-MM-ddTHH:mm:ss.fff`)

#### Examples
* `ofType` &rarr; `Given foo is of type "string"`
* `equalTo` &rarr; `Given foo is equal to 5`
* `inSet` &rarr; 
```
Given foo is in set: 
  | foo | 
  | 1 | 
  | 2 | 
  | 3 |
```
* `not(after 01/02/2003)` &rarr; `Given foo is anything but after 2003-02-01T00:00:00.00`

In addition the following shows how the _there is a constraint_ step can be used:
```
And there is a constraint:
       """
         {
         "if": { "field": "foo", "is": "equalTo", "value": "dddd" },
         "then": { "field": "bar", "is": "equalTo", "value": "4444" },
         "else": { "field": "bar", "is": "shorterThan", "value": 1 }
         }
       """
```

### Describing the outcome
* _the profile is invalid_, executes the generator and asserts that any of the following exceptions were thrown: `InvalidProfileException`, `JsonParseException`, `IllegalArgumentException`, `ClassCastException`. I.e. something went wrong when trying to read or parse the profile
* _the profile is invalid because "`{reason}`"_, executes the generator and asserts that an `InvalidProfileException` was thrown with the message `{reason}`.
* _I am presented with an error message_, executes the generator and asserts that a exception was thrown, doesn't discriminate over the type of exception
* _no data is created_, executes the generator and asserts that no data was emitted
* _the following data should be generated:_, executes the generator and asserts that no exceptions were thrown and the given data appears in the generated data, no additional data is permitted.
* _the following data should be generated in order:_, executes the generator and asserts that no exceptions were thrown and the given data appears **in the same order** in the generated data, no additional data is permitted.
* _the following data should be included in what is generated:_, executes the generator and asserts that no exceptions were thrown and the given data is present in the generated data (regardless of order)
* _the following data should not be included in what is generated:_, executes the generator and asserts that no exceptions were thrown and the given data is **not** present in the generated data (regardless of order)
* _some data should be generated_, executes the generator and asserts that at least one row of data was emitted
* _{number} of rows of data are generated_, executes the generator and asserts that exactly the given number of rows are generated

### Validating the data in the output

#### Datetime
* _{field} contains datetime data_, executes the generator and asserts that _field_ contains either `null` or datetime values
* _{field} contains anything but datetime data_, executes the generator and asserts that _field_ contains either `null` or data that is not datetime.
* _{field} contains datetime values between {min} and {max} inclusively_, executes the generator and asserts that _field_ contains either `null` or datetime values between _{min}_ and _{max}_. Does so in an inclusive manner for both min and max.
* _{field} contains datetime values outside {min} and {max}_, executes the generator and asserts that _field_ contains either `null` or datetime values outside _{min}_ and _{max}_.
* _{field} contains datetime values before or at {before}_, executes the generator and asserts that _field_ contains either `null` or datetime values at or before _{before}_
* _{field} contains datetime values after or at {after}_, executes the generator and asserts that _field_ contains either `null` or datetime values at or after _{after}_

#### Numeric
* _{field} contains numeric data_, executes the generator and asserts that _field_ contains either `null` or numeric values
* _{field} contains anything but numeric data_, executes the generator and asserts that _field_ contains either `null` or data that is not numeric.
* _{field} contains numeric values between {min} and {max} inclusively_, executes the generator and asserts that _field_ contains either `null` or numeric values between _{min}_ and _{max}_. Does so in an inclusive manner for both min and max.
* _{field} contains numeric values outside {min} and {max}_, executes the generator and asserts that _field_ contains either `null` or numeric values outside _{min}_ and _{max}_.
* _{field} contains numeric values less than or equal to {value}_, executes the generator and asserts that _field_ contains either `null` or numeric values less than or equal to _{value}_
* _{field} contains numeric values greater than or equal to {value}_, executes the generator and asserts that _field_ contains either `null` or numeric values greater than or equal to _{value}_

#### String
* _{field} contains string data_, executes the generator and asserts that _field_ contains either `null` or string values
* _{field} contains anything but string data_, executes the generator and asserts that _field_ contains either `null` or data that is not a string.
* _{field} contains strings of length between {min} and {max} inclusively_, executes the generator and asserts that _field_ contains either `null` or strings with lengths between _{min}_ and _{max}_. Does so in an inclusive manner for both min and max.
* _{field} contains strings of length outside {min} and {max}_, executes the generator and asserts that _field_ contains either `null` or strings with lengths outside _{min}_ and _{max}_.
* _{field} contains strings matching /{regex}/_, executes the generator and asserts that _field_ contains either `null` or strings that match the given regular expression.
* _{field} contains anything but strings matching /{regex}/_, executes the generator and asserts that _field_ contains either `null` or strings that do not match the given regular expression.
* _{field} contains strings shorter than or equal to {length}_, executes the generator and asserts that _field_ contains either `null` or string values shorter than or equal to _{length}_
* _{field} contains strings longer than or equal to {length}_, executes the generator and asserts that _field_ contains either `null` or string values longer than or equal to _{length}_

#### Null (absence/presence)
* _{field} contains anything but null_, executes the generator and asserts that _field_ has a value in every row (i.e. no `null`s)

### Cucumber test style guide
* Tests should be written to validate one piece of logic only per test.
* Tests should specify definite expected results rather than using "should include".
* All tables should use pipe characters "|" to denote the edges of the table and the table should be sized according to the largest item in the table.
* The indentation of cucumber tests should be:
>   * **Feature / Background / Scenario / Scenario outline** - 0 spaces
>   * **Given / When / Then** - 5 spaces
>   * **And** - 7 spaces
>   * **Tables / JSON snippets** - 2 spaces indented from the above statement
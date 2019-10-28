# Cucumber syntax

We use cucumber for behaviour driven development and testing, with [gherkin](https://docs.cucumber.io/gherkin/)-based tests like the below:

```gherkin
Feature: the name of my feature

  Background:
    Given the generation strategy is interesting
    And there is a non nullable field foo

  Scenario: Running the generator should emit the correct data
    Given foo is equal to 8
    Then the following data should be generated:
      | foo  |
      | 8    |
      | null |
```

More examples can be seen in the [generator cucumber features](https://github.com/finos/datahelix/tree/master/orchestrator/src/test/java/com/scottlogic/deg/orchestrator/cucumber)

The framework supports setting configuration settings for the generator, defining the profile and describing the expected outcome. All of these are described below, all variable elements (e.g. `{generationStrategy}` are case insensitive), all fields and values **are case sensitive**.

### Configuration options
* _the generation strategy is `{generationStrategy}`_ see [generation strategies](../user/generationTypes/GenerationTypes.md) - default: `random`
* _the combination strategy is `{combinationStrategy}`_ see [combination strategies](../user/CombinationStrategies.md) - default: `exhaustive`
* _the data requested is `{generationMode}`_, either `violating` or `validating` - default: `validating`
* _the generator can generate at most `{int}` rows_, ensures that the generator will only emit `int` rows, default: `1000`
* _we do not violate constraint `{operator}`_, prevent this operator from being violated (see **Operators** section below), you can specify this step many times if required

### Defining the profile
It is important to remember that constraints are built up of 3 components: a field, an operator and most commonly an operand. In the following example the operator is 'greaterThan' and the operand is 5.

```
foo is greaterThan 5
```

Operators are converted to English language equivalents for use in cucumber, so 'greaterThan' is expressed as 'greater than'.

* _there is a non nullable field `{field}`_, adds a field called `field` to the profile
* _the following non nullable fields exist:_, adds a set of fields to the profile (is followed by a single column set of strings, each represents a field name)
* _`{field}` is null_, adds a null constraint to the profile for the field `field`
* _`{field}` is anything but null_, adds a not(is null) constraint to the profile for field `field`
* _`{field}` is `{operator}` `{operand}`_, adds an `operator` constraint to the field `field` with the data `operand`, see **operators** and **operands** sections below
* _`{field}` is anything but `{operator}` `{operand}`_, adds a negated `operator` constraint to the field `field` with the data `operand`, see **operators** and **operands** sections below
* _there is a constraint:_, adds the given JSON block as a constraint as if it was read from the profile file itself. It should only be used where the constraint cannot otherwise be expressed, e.g. for `anyOf`, `allOf` and `if`.
* _the maximum string length is {length}_, sets the maximum length for strings to the _max_ for the given scenario. The default is _200_ (for performance reasons), however in production the limit is _1000_.
* _untyped fields are allowed_, sets the --allow-untyped-fields flag to false - default: flag is true

#### Operators
See [Predicate constraints](../user/UserGuide.md#Predicate-constraints), [Grammatical Constraints](../user/UserGuide.md#Grammatical-constraints) and [Presentational Constraints](../user/UserGuide.md#Presentational-constraints) for details of the constraints.

#### Operands
When specifying the operator/s for a field, ensure to format the value as in the table below:

| data type | example |
| ---- | ---- |
| string | "my value" |
| number | `1.234` |
| datetime | `2001-02-03T04:05:06.000` |
| null | `null` |

datetimes must be expressed as above (i.e. `yyyy-MM-ddTHH:mm:ss.fff`)

#### Examples
* `ofType` &rarr; `Given foo has type "string"`
* `equalTo` &rarr; `Given foo is equal to 5`
* `inSet` &rarr; 
```
Given foo is in set: 
  | foo |
  | 1   |
  | 2   |
  | 3   |
```
* `not(after 01/02/2003)` &rarr; `Given foo is anything but after 2003-02-01T00:00:00.00`

In addition the following shows how the _there is a constraint_ step can be used:
```
And there is a constraint:
  """
    {
      "if": { "field": "foo", "equalTo": "dddd" },
      "then": { "field": "bar", "equalTo": "4444" },
      "else": { "field": "bar", "is": "shorterThan", "value": 1 }
    }
  """
```

### Describing the outcome
* _the profile is invalid because "`{reason}`"_, executes the generator and asserts that an `InvalidProfileException` or `JsonParseException` was thrown with the message `{reason}`, reason is a regular expression*.
* _no data is created_, executes the generator and asserts that no data was emitted
* _the following data should be generated:_, executes the generator and asserts that no exceptions were thrown and the given data appears in the generated data, no additional data is permitted.
* _the following data should be generated in order:_, executes the generator and asserts that no exceptions were thrown and the given data appears **in the same order** in the generated data, no additional data is permitted.
* _the following data should be included in what is generated:_, executes the generator and asserts that no exceptions were thrown and the given data is present in the generated data (regardless of order)
* _the following data should not be included in what is generated:_, executes the generator and asserts that no exceptions were thrown and the given data is **not** present in the generated data (regardless of order)
* _some data should be generated_, executes the generator and asserts that at least one row of data was emitted
* _{number} of rows of data are generated_, executes the generator and asserts that exactly the given number of rows are generated

\* Because `{reason}` is a regular expression, certain characters will need to be escaped, by including a `\` before them, e.g. `\(`, `\)`, `\[`, `\]`, etc.

### Validating the data in the output

#### DateTime
* _{field} contains datetime data_, executes the generator and asserts that _field_ contains either `null` or datetimes (other types are allowed)
* _{field} contains only datetime data_, executes the generator and asserts that _field_ contains only `null` or datetimes
* _{field} contains anything but datetime data_, executes the generator and asserts that _field_ contains either `null` or data that is not a datetime.
* _{field} contains datetimes between {min} and {max} inclusively_, executes the generator and asserts that _field_ contains either `null` or datetimes between _{min}_ and _{max}_. Does so in an inclusive manner for both min and max.
* _{field} contains datetimes outside {min} and {max}_, executes the generator and asserts that _field_ contains either `null` or datetimes outside _{min}_ and _{max}_.
* _{field} contains datetimes before or at {before}_, executes the generator and asserts that _field_ contains either `null` or datetimes at or before _{before}_
* _{field} contains datetimes after or at {after}_, executes the generator and asserts that _field_ contains either `null` or datetimes at or after _{after}_

#### Numeric
Note these steps work for asserting both integer and decimal data. There are no current steps for asserting general granularity.
* _{field} contains numeric data_, executes the generator and asserts that _field_ contains either `null` or numeric values (other types are allowed)
* _{field} contains only numeric data_, executes the generator and asserts that _field_ contains only `null` or numeric values
* _{field} contains anything but numeric data_, executes the generator and asserts that _field_ contains either `null` or data that is not numeric.
* _{field} contains numeric values between {min} and {max} inclusively_, executes the generator and asserts that _field_ contains either `null` or numeric values between _{min}_ and _{max}_. Does so in an inclusive manner for both min and max.
* _{field} contains numeric values outside {min} and {max}_, executes the generator and asserts that _field_ contains either `null` or numeric values outside _{min}_ and _{max}_.
* _{field} contains numeric values less than or equal to {value}_, executes the generator and asserts that _field_ contains either `null` or numeric values less than or equal to _{value}_
* _{field} contains numeric values greater than or equal to {value}_, executes the generator and asserts that _field_ contains either `null` or numeric values greater than or equal to _{value}_

#### String
* _{field} contains string data_, executes the generator and asserts that _field_ contains either `null` or string values (other types are allowed)
* _{field} contains only string data_, executes the generator and asserts that _field_ contains only `null` or string values
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
* Each test should be specific to one requirement.
* Tests should specify definite expected results rather than using "should include".
* All tables should be padded to the width of the largest item.
* All block-level indentation should be 2 spaces, as below: 

```gherkin
Feature: ...
  ...

  Background:
    Given ...

  Scenario: ...
    Given ...:
      | ... |
      | ... |
      | ... |
    And ...:
      """
      """
    When ...
    Then ... 
    And ...
```

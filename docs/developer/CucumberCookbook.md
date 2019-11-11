
# Cucumber Cookbook

This document outlines how Cucumber is used within DataHelix.

The framework supports setting configuration settings for the generator, defining the profile and describing the expected outcome. All of these are described below, all variable elements (e.g. `{generationStrategy}` are case insensitive), all fields and values **are case sensitive**.

### Configuration options
* _the generation strategy is `{generationStrategy}`_ see [generation strategies](https://github.com/finos/datahelix/blob/master/docs/UserGuide.md/#generation-strategies.md) - default: `random`
* _the combination strategy is `{combinationStrategy}`_ see [combination strategies](https://github.com/finos/datahelix/blob/master/docs/UserGuide.md/#Combination-strategies.md) - default: `exhaustive`
* _the generator can generate at most `{int}` rows_, ensures that the generator will only emit `int` rows, default: `1000`

### Defining the profile
It is important to remember that constraints are built up of 3 components: a field, an operator and most commonly an operand. In the following example the operator is 'greaterThan' and the operand is 5.

```
foo is greaterThan 5
```

Operators are converted to English language equivalents for use in cucumber, so 'greaterThan' is expressed as 'greater than'.

* _there is a non nullable field `{field}`_, adds a field called `field` to the profile
* _the following nullable fields exist:_, adds a set of fields to the profile (is followed by a single column set of strings, each represents a field name)
* _`{field}` is null_, adds a null constraint to the profile for the field `field`
* _`{field}` is anything but null_, adds a not(is null) constraint to the profile for field `field`
* _`{field}` is `{operator}` `{operand}`_, adds an `operator` constraint to the field `field` with the data `operand`, see **operators** and **operands** sections below
* _`{field}` is anything but `{operator}` `{operand}`_, adds a negated `operator` constraint to the field `field` with the data `operand`, see **operators** and **operands** sections below
* _there is a constraint:_, adds the given JSON block as a constraint as if it was read from the profile file itself. It should only be used where the constraint cannot otherwise be expressed, e.g. for `anyOf`, `allOf` and `if`.
* _the maximum string length is {length}_, sets the maximum length for strings to the _max_ for the given scenario. The default is _200_ (for performance reasons), however in production the limit is _1000_.


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

### Grammatical Constraints

Grammatical constraints (`anyOf`, `allOf`, and `if`) are supported within the cucumber hooks for more complex behaviour. These hooks expect constraints to follow the step and these constraints are grouped into the grammatical constraint. All of these hooks can be nested.

### if

* _If and Then are described below_, adds an if constraint to the profile. This expects 2 contraints to follow the step with the first constraint being mapped to the if statement and the second constraint to the then block.
* _If Then and Else are described below_, adds an if constraint to the profile. This expects 3 contraints to follow the step with the first constraint being mapped to the if statement, the second constraint to the then block, and the third constraint to the else block.

Below shows how the conversion from a JSON profile to the steps should appear:

__Json Constraint__

```json
{
  "if": { "field": "foo", "equalTo": "dddd" },
  "then": { "field": "bar", "equalTo": "4444" },
  "else": { "field": "bar", "is": "shorterThan", "value": 1 }
}
```

__Cucumber Steps__

```json
When If Then and Else are described below
And foo is equal to "dddd"
And bar is equal to "4444"
And bar is shorter than 1
```

#### anyOf

* _Any Of the next {number} constraints_, adds an anyOf constraint to the profile. The next x number of constraints will be added to the constraint where x is the provided number

Below shows how the conversion from a JSON profile to the steps should appear:

__Json Constraint__

```json
{
  "anyOf": [
    { "field": "foo", "equalTo": "Test0" },
    { "field": "foo", "is": "matchingRegex", "value": "[a-b]{4}" }
  ]
}
```

__Cucumber Steps__

```json
    And Any Of the next 2 constraints
    And foo is equal to "Test0"
    And foo is matching regex "[a-b]{4}"
```

#### allOf

* _All Of the next {number} constraints_, adds an allOf constraint to the profile. The next x number of constraints will be added to the constraint where x is the provided number

Below shows how the conversion from a JSON profile to the steps should appear:

__Json Constraint__

```json
{
  "allOf": [
    { "field": "foo", "equalTo": "Test0" },
    { "field": "foo", "is": "matchingRegex", "value": "[a-b]{4}" }
  ]
}
```

__Cucumber Steps__

```json
    And All Of the next 2 constraints
    And foo is equal to "Test0"
    And foo is matching regex "[a-b]{4}"
```

#### Nesting Behaviour

The grammatical constraints can be nested with the processing behaving like Polish notation, an example can be seen below:

__Json Constraint__

```json
{
  "allOf": [
    {
      "anyOf": [
        { "field": "foo", "equalTo": "Test0" },
        { "field": "foo", "equalTo": "Test2" },
        { "field": "foo", "equalTo": "Test4" }
      ]
    },
    { "field": "foo", "is": "matchingRegex", "value": "[a-b]{4}" }
  ]
}
```

__Cucumber Steps__

```json
    And All Of the next 2 constraints
    And Any Of the next 3 constraints
      And foo is equal to "Test0"
      And foo is equal to "Test2"
      And foo is equal to "Test4"
    And foo is matching regex "[a-b]{4}"
```

### Describing the outcome
* _the profile is invalid because "`{reason}`"_, executes the generator and asserts that an `ValidationException` or `JsonParseException` was thrown with the message `{reason}`, reason is a regular expression*.
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

# Table of Contents
1. [Getting Started](gettingStarted/StepByStepInstructions.md)
1. [Profiles](#Profiles)
    1. [Fields](#Fields)
    2. [Rules](#Rules)
    3. [Persistence](#Persistence)
    4. [Creation](#Creation)

2. [Data types](#Data-Types)
    1. [Integer/Decimal](#Integer/Decimal)
    2. [Strings](#Strings)
    3. [DateTime](#DateTime)
    4. [Financial Codes](#FinancialCodes)
    5. [Personal Data Types](#PersonalDataTypes)

3. [Predicate constraints](#Predicate-constraints)
    1. [Theory](#Theory)
    2. [General constraints](#General-constraints)
        1. [equalTo](#predicate-equalto)
        2. [inSet](#predicate-inset)
        3. [null](#predicate-null)
        4. [ofType](#predicate-oftype)
    3. [Textual constraints](#Textual-constraints)
        1. [matchingRegex](#predicate-matchingregex)
        2. [containingRegex](#predicate-containingregex)
        3. [ofLength](#predicate-oflength)
        4. [longerThan](#predicate-longerthan)
        5. [shorterThan](#predicate-shorterthan)
    4. [Integer/Decimal constraints](#Integer/Decimal-constraints)
        1. [greaterThan](#predicate-greaterthan)
        2. [greaterThanOrEqualTo](#predicate-greaterthanorequalto)
        3. [lessThan](#predicate-lessthan)
        4. [lessThanOrEqualTo](#predicate-lessthanorequalto)
        5. [granularTo](#predicate-granularto)
    5. [DateTime constraints](#DateTime-constraints)
        1. [after](#predicate-after)
        2. [afterOrAt](#predicate-afterorat)
        3. [before](#predicate-before)
        4. [beforeOrAt](#predicate-beforeorat)
        5. [granularTo](#predicate-granularto-datetime)

4. [Grammatical constraints](#Grammatical-Constraints)
    1. [not](#not)
    2. [anyOf](#anyOf)
    3. [allOf](#allOf)
    4. [if](#if)

5. [Presentational constraints](#Presentational-Constraints)

6. [Profile Validation](#Profile-Validation)
    1. [JetBrains IntelliJ](#JetBrains-IntelliJ)
    2. [Microsoft Visual Studio Code](#Microsoft-Visual-Studio-Code)
    3. [Schema Validation using library](#Schema-Validation-using-library)

# Profiles

**Data profiles** describe potential or real data. For instance, we could design a profile that describes a _user_account_ table, where:

* the data must have _user_id_, _email_address_ and _creation_date_ fields
* _user_id_ is a non-optional string
* _email_address_ must be populated and contain a @ character
  * however, if _user_id_ is itself an email address, _email_address_ must be absent
* _creation_date_ is a non-optional date, with no time component, later than 2003 and output per [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601)    

This data profile can be found in the [examples folder](../../examples/user-account).

Every profile declares some **fields** and some **constraints**.

## Fields

Fields are the "slots" of data that can take values. If generating into a CSV or database, fields are columns. If generating into JSON, fields are object properties. Typical fields might be _email_address_ or _user_id_.

By default, any piece of data is valid for a field. To get more specific, we need to add **rules**.

## Rules

Every rule is a named collection of *constraints*, which can be any of:

* [Predicate constraints](#Predicate-constraints), which limit a field's possible values
* [Presentational constraints](#Presentational-Constraints), which control how values are serialised (eg, number of significant figures)
* [Grammatical constraints](#Grammatical-constraints), which combine other constraints together

(Predicate and formatting constraints are collectively referred to as **data constraints**)

The decision of how to group constraints into rules is up to the user. At the extremes, there could be a separate rule for each constraint, or one rule containing every constraint. More usually, rules will represent collections of related constraints (eg, _"X is a non-null integer between 0 and 100"_ is a fine rule, comprising four constraints). How to group into rules becomes particularly important when [deliberate violation](alphaFeatures/DeliberateViolation.md) is involved.

## Persistence

Profiles are persisted as JSON documents following a [schema](Schema.md) saved as UTF-8.

## Creation

Profiles can be created by any of:

- Writing JSON profiles by hand or by some custom transform process
- ~~Deriving them by supplying some sample data to the Profiler~~ (not yet)
- ~~Designing them through the web front end~~ (not yet)

# Data Types

DataHelix currently recognises four core data types: _string_, _datetime_, _integer_ and _decimal_.  It also recognises more complex data types which are extensions of these core types.  At present, all such data types are extensions of the _string_ type.

## Integer/Decimal

Within a profile, users can specify two numeric data types: integer and decimal. Under the hood both of these data types are considered numeric from a point of generation but the integer type enforces a granularity of 1, see below for more information on granularity.

Decimals and integers have a maximum value of 1E20, and a minimum value of -1E20.

In profile files, numbers must be expressed as JSON numbers, without quotation marks.

### Numeric granularity

The granularity of a numeric field is a measure of how small the distinctions in that field can be; it is the smallest positive number of which every valid value is a multiple. For instance:

- if a numeric field has a granularity of 1, it can only be satisfied with multiples of 1; the integer data type adds this constraint by default
- if a decimal field has a granularity of 0.1, it can be satisfied by (for example) 1, 2, 1.1 or 1.2, but not 1.11 or 1.12

Granularities must be powers of ten less than or equal to one (1, 0.1, 0.01, etc). Note that it is possible to specify these granularities in scientific format eg 1E-10, 1E-15 where the _10_ and _15_ clearly distinguish that these numbers can have up to _10_ or _15_ decimal places respectively. Granularities outside these restrictions could be potentially useful (e.g. a granularity of 2 would permit only even numbers) but are not currently supported. 

Decimal fields currently default to the maximum granularity of 1E-20 (0.00000000000000000001) which means that numbers can be produced with up to 20 decimal places. This numeric granularity also dictates the smallest possible step between two numeric values, for example the next biggest decimal than _10_ is _10.00000000000000000001_. A user is able to add a `granularTo` constraint for a decimal value with coarser granularity (1, 0.1, 0.01...1E-18, 1E-19) but no finer granularity than 1E-20 is allowed.

Note that granularity concerns which values are valid, not how they're presented. If the goal is to enforce a certain number of decimal places in text output, the `formattedAs` operator is required. See: [What's the difference between formattedAs and granularTo?](./FrequentlyAskedQuestions.md#whats-the-difference-between-formattedas-and-granularto)

## Strings

Strings are sequences of unicode characters with a maximum length of 1000 characters. Currently, only basic latin characters (unicode 002c - 007e) are supported.

## DateTime

DateTimes represent specific moments in time, and are specified in profiles through specialised objects:

```javascript
{ "date": "2000-01-01T09:00:00.000" }
```

The format is a subset of [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601); the date and time must be fully specified as above, 
with precisely 3 digits of sub-second precision, plus an optional offset specifier of "Z". All datetimes are treated as UTC.

DateTimes can be in the range `0001-01-01T00:00:00.000Z` to `9999-12-31T23:59:59.999Z`
that is **_`midnight on the 1st January 0001`_** to **_`1 millisecond to midnight on the 31 December 9999`_**.

### DateTime granularity

The granularity of a DateTime field is a measure of how small the distinctions in that field can be; it is the smallest positive unit of which every valid value is a multiple. For instance:

- if a DateTime field has a granularity of years, it can only be satisfied by dates that are complete years (e.g. `2018-01-01T00:00:00.000Z`)
- if a decimal field has a granularity of days, it can be satisfied by (for example) `2018-02-02T00:00:00.000Z` or `2018-02-03T00:00:00.000Z`, but not `2018-02-02T01:00:00.000Z` or `2018-02-02T00:00:00.001Z`

Granularities must be one of the units: millis, seconds, minutes, hours, days, months, years.

DateTime fields currently default to the most precise granularity of milliseconds. A user is able to add a `granularTo` constraint for a DateTime value with coarser granularity (seconds, minutes...years) but no finer granularity than milliseconds is currently allowed.

Note that granularity concerns which values are valid, not how they're presented. All values will be output with the full format defined by [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601), so that a value granular to years will still be output as (e.g.) `0001-01-01T00:00:00.000Z`, rather than `0001` or `0001-01-01`.

## Financial Codes

Data Helix currently recognises and can generate a number of types of financial code:

- ISIN
- SEDOL
- CUSIP
- RIC

When this is specified as the type of a field, the data generated will contain legal values for the type of code in question (with correct check digits, where appropriate), but the codes generated may or may not be allocated in the real world.

Data Helix currently only supports ISIN codes in the `GB` and `US` ranges.  Only codes in these ranges will be generated.

## Personal Data Types

Data Helix can generate data containing typical real names, based on recent demographic data, by defining a field as being of the types `firstname`, `lastname` or `fullname`.  Name fields are strings and can be combined with other textual constraints to generate, for example, first names that are longer than 6 letters.

The only string considered to be an invalid name is the empty string.

# Predicate constraints

## Theory

* A **predicate constraint** defines any given value as being _valid_ or _invalid_
* The **universal set** contains all values that can be generated (`null`, any string, any date, any number, etc)
* The **denotation** of a constraint is the subset of the universal set that it defines as valid

See [set restriction and generation](SetRestrictionAndGeneration.md) for an in depth explanation of how the constraints are merged and data generated from them.

If no constraints are defined over a field, then it can accept any member of the universal set. Each constraint added to that field progressively limits the universal set.

The [grammatical `not` constraint](#Grammatical-Constraints) inverts a constraint's denotation; in other words, it produces the complement of the constraint's denotation and the universal set.

## General constraints

<div id="predicate-equalto"></div>

### `equalTo` _(field, value)_  

```javascript
{ "field": "type", "is": "equalTo", "value": "X_092" }
OR
{ "field": "type", "is": "equalTo", "value": 23 }
OR
{ "field": "type", "is": "equalTo", "value": { "date": "2001-02-03T04:05:06.007" } }
```

Is satisfied if `field`'s value is equal to `value`

<div id="predicate-inset"></div>

### `inSet` _(field, values)_

```javascript
{ "field": "type", "is": "inSet", "values": [ "X_092", 123, null, { "date": "2001-02-03T04:05:06.007" } ] }
```

Is satisfied if `field`'s value is in the set `values`

Alternatively, sets can be populated from files.

```javascript
{ "field": "country", "is": "inSet", "file": "countries.csv" }
```

Populates a set from the new-line delimited file (with suffix `.csv`), where each line represents a string value to load.
The file should be location in the same directory as the jar, or in the directory explicitly specified using the command line argument `--set-from-file-directory`, and the name should match the `value` with `.csv` appended.
Alternatively an absolute path can be used which does not have any relation to the jar location.
In the above example, this would be `countries.csv`.

Example `countries.csv` excerpt:
```javascript
...
England
Wales
Scotland
...
```

Additionally, weights can be included in the source file, which will then weight each element proportionally to its weight.
Example `countries_weighted.csv` excerpt:
```javascript
...
England, 2
Wales, 1
Scotland, 3
...
```

After loading the set from the file, this constraint behaves identically to the [inSet](#predicate-inset) constraint. This includes its behaviour when negated or violated.

<div id="predicate-null"></div>

### `null` _(field)_

```javascript
{ "field": "price", "is": "null" }
```

Is satisfied if `field` is null or absent.

<div id="predicate-oftype"></div>

### `ofType` _(field, value)_

```javascript
{ "field": "price", "is": "ofType", "value": "string" }
```

Is satisfied if `field` is of type represented by `value` (valid options: `decimal`, `integer`, `string`, `datetime`, `ISIN`, `SEDOL`, `CUSIP`, `RIC`, `firstname`, `lastname` or `fullname`)

## Textual constraints

<div id="predicate-matchingregex"></div>

### `matchingRegex` _(field, value)_

```javascript
{ "field": "name", "is": "matchingRegex", "value": "[a-z]{0, 10}" }
```

Is satisfied if `field` is a string matching the regular expression expressed in `value`. The regular expression must match the entire string in `field`, start and end anchors `^` & `$` are ignored.

The following non-capturing groups are unsupported:
- Negative look ahead/behind, e.g. `(?!xxx)` and `(?<!xxx)`
- Positive look ahead/behind, e.g. `(?=xxx)` and `(?<=xxx)`

<div id="predicate-containingregex"></div>

### `containingRegex` _(field, value)_

```javascript
{ "field": "name", "is": "containingRegex", "value": "[a-z]{0, 10}" }
```

Is satisfied if `field` is a string containing the regular expression expressed in `value`. Using both start and end anchors `^` & `$` make the constraint behave like `matchingRegex`.

The following non-capturing groups are unsupported:
- Negative look ahead/behind, e.g. `(?!xxx)` and `(?<!xxx)`
- Positive look ahead/behind, e.g. `(?=xxx)` and `(?<=xxx)`

<div id="predicate-oflength"></div>

### `ofLength` _(field, value)_

```javascript
{ "field": "name", "is": "ofLength", "value": 5 }
```

Is satisfied if `field` is a string whose length exactly matches `value`, must be a whole number between `0` and `1000`.

<div id="predicate-longerthan"></div>

### `longerThan` _(field, value)_

```javascript
{ "field": "name", "is": "longerThan", "value": 3 }
```

Is satisfied if `field` is a string with length greater than `value`, must be a whole number between `-1` and `999`.

<div id="predicate-shorterthan"></div>

### `shorterThan` _(field, value)_

```javascript
{ "field": "name", "is": "shorterThan", "value": 3 }
```

Is satisfied if `field` is a string with length less than `value`, must be a whole number between `1` and `1001`.   

## Integer/Decimal constraints

<div id="predicate-greaterthan"></div>

### `greaterThan` _(field, value)_

```javascript
{ "field": "price", "is": "greaterThan", "value": 0 }
```

Is satisfied if `field` is a number greater than `value`.

<div id="predicate-greaterthanorequalto"></div>

### `greaterThanOrEqualTo` _(field, value)_

```javascript
{ "field": "price", "is": "greaterThanOrEqualTo", "value": 0 }
```

Is satisfied if `field` is a number greater than or equal to `value`.

<div id="predicate-lessthan"></div>

### `lessThan` _(field, value)_

```javascript
{ "field": "price", "is": "lessThan", "value": 0 }
```

Is satisfied if `field` is a number less than `value`.

<div id="predicate-lessthanorequalto"></div>

### `lessThanOrEqualTo` _(field, value)_

```javascript
{ "field": "price", "is": "lessThanOrEqualTo", "value": 0 }
```

Is satisfied if `field` is a number less than or equal to `value`.

<div id="predicate-granularto"></div>

### `granularTo` _(field, value)_

```javascript
{ "field": "price", "is": "granularTo", "value": 0.1 }
```

Is satisfied if `field` has at least the [granularity](#Numeric-granularity) specified in `value`.

## DateTime constraints
All dates must be in format `yyyy-MM-ddTHH:mm:ss.SSS` and embedded in a _date-object_. The Z suffix can be included, but is not required. All datetimes are treated as UTC whether the Z suffix is included or not.

Example: `{ "date": "2001-02-03T04:05:06.007" }`

<div id="predicate-after"></div>

### `after` _(field, value)_

```javascript
{ "field": "date", "is": "after", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring after `value`.

<div id="predicate-afterorat"></div>

### `afterOrAt` _(field, value)_

```javascript
{ "field": "date", "is": "afterOrAt", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring after or simultaneously with `value`.

<div id="predicate-before"></div>

### `before` _(field, value)_

```javascript
{ "field": "date", "is": "before", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring before `value`.

<div id="predicate-beforeorat"></div>

### `beforeOrAt` _(field, value)_

```javascript
{ "field": "date", "is": "beforeOrAt", "value": { "date": "2018-09-01T00:00:00.000" } }
```

Is satisfied if `field` is a datetime occurring before or simultaneously with `value`.

<div id="predicate-granularto-datetime"></div>

### `granularTo` _(field, value)_

```javascript
{ "field": "date", "is": "granularTo", "value": "days" }
```

Is satisfied if `field` has at least the [granularity](#DateTime-granularity) specified in `value`.

# Grammatical constraints
<div id="Grammatical-constraints"></div>

**Grammatical constraints** combine or modify other constraints. They are fully recursive; any grammatical constraint is a valid input to any other grammatical constraint.

See [set restriction and generation](SetRestrictionAndGeneration.md) for an in depth explanation of how the constraints are merged and data generated from them.

## `not`

```javascript
{ "not": { "field": "foo", "is": "null" } }
```

Wraps a constraint. Is satisfied if, and only if, its inner constraint is _not_ satisfied.

## `anyOf`

```javascript
{ "anyOf": [
    { "field": "foo", "is": "null" },
    { "field": "foo", "is": "equalTo", "value": 0 }
]}
```

Contains a number of sub-constraints. Is satisfied if any of the inner constraints are satisfied.

## `allOf`

```javascript
{ "allOf": [
    { "field": "foo", "is": "ofType", "value": "integer" },
    { "field": "foo", "is": "equalTo", "value": 0 }
]}
```

Contains a number of sub-constraints. Is satisfied if all of the inner constraints are satisfied.

## `if`

```javascript
{
    "if":   { "field": "foo", "is": "ofType", "value": "integer" },
    "then": { "field": "foo", "is": "greaterThan", "value": 0 },
    "else": { "field": "foo", "is": "equalTo", "value": "N/A" }
}
```

Is satisfied if either:
 
- Both the `if` and `then` constraints are satisfied
- The `if` constraint is not satisfied, and the `else` constraint is

While it's not prohibited, wrapping conditional constraints in any other kind of constraint (eg, a `not`) may cause unintuitive results.


# Presentational Constraints
<div id="Presentational-constraints"></div>

### `formattedAs` _(field, value)_

```javascript
{ "field": "price", "is": "formattedAs", "value": "%.5s" }
```

Used by output serialisers where string output is required.

For the formatting to be applied, the generated data must be applicable, and the `value` must be:

* a string recognised by Java's `String.format` method
* appropriate for the data type of `field`
* not `null` (formatting will not be applied for null values)

Formatting will not be applied if not applicable to the field's value

See the [FAQ](FrequentlyAskedQuestions.md) for the difference between this and [granularTo](#predicate-granularto).

# Profile Validation

The [JSON schema](https://json-schema.org/) for the DataHelix data profile is stored in the file [`datahelix.schema.json`](../profile/src/main/resources/profileschema/0.1/datahelix.schema.json) in the [schemas module](../profile/src/main/resources/profileschema/0.1/) directory.

## JetBrains IntelliJ

**_Although IntelliJ tries to validate the profile json files against the schema, it incorrectly shows the whole profile as invalid instead of specific errors._**

**_For this reason we recommend using Visual Studio Code for writing and editing profiles._**


To use the DataHelix profile JSON schema in IntelliJ we need to  set up the intellij editor to validate all json files under the `json` and/or `examples` directories against the `datahelix.schema.json` schema file.

To setup IntelliJ to validate json files against the schema follow these steps:

1. Open IntelliJ
1. Select `File` -> `Settings` -> `Languages & Frameworks` -> `Schemas and DTDs`
1. Select `JSON Schema Mappings`
1. Press the `+` button to add a new schema mapping
1. Give the mapping a name (e.g. `DataHelix Profile Schema`)
1. For `Schema file or URL:` select the local schema file (e.g. `<project root>/datahelix/profile/src/main/resources/profileschema/0.1/datahelix.schema.json`)
1. Make sure the `Schema version:` is set to `JSON schema version 7`
1. Press the `+` button to add a new mapping
1. Select `Add Directory`
1. Select the `json` directory
1. Press okay

Now when you open a json file from the `json` directory in IntelliJ, it will be automatically validated against the DataHelix profile schema.


## Microsoft Visual Studio Code

To enable visual studio code to validate json files against the DataHelix profile schema a `json.schemas` section needs to be added to the `settings.json` file.

To do this:

1. Click on the gear icon at the bottom left of the screen and select `Settings`
1. In the settings windows, click `Extensions` -> `JSON`
1. You should see a section like this:
    ```
    Schemas
    Associate schemas to JSON files in the current project
    Edit in settings.json
    ```
1. Click on the `Edit in settings.json` link and VSCode will open the settings.json file.
1. Add the following snippet to the end of the file (replacing `<datahelix_projectroot>` with the root directory path for the DataHelix project and replacing the `"fileMatch"` value with an appropriate value for your configuration):
    ```
      "json.schemas": [
        {
          "fileMatch": [
            "<datahelix_projectroot>/*"
          ],
          "url": "file:///<datahelix_projectroot>/profile/src/main/resources/profileschema/0.1/datahelix.schema.json"
        }
      ]
    ```
    Alternatively you can configure this to any naming convention you want for profile files, for example `"*.profile.json"`.

    To verify that the url to the `datahelix.schema.json` is valid you can `ctrl-click` on it and the schema file will open in the editor.  
1. If the ` "json.schemas"` snippet already exists, you can add a new object to the JSON array for the DataHelix profile schema.

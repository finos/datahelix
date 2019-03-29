# Table of Contents
1. [Profiles](#Profiles)
    1. [Fields](#Fields)
    2. [Rules](#Rules)
    3. [Persistence](#Persistence)
    4. [Creation](#Creation)

2. [Data types](#Data-Types)
    1. [Integer/Decimal](#Integer/Decimal)
    2. [Strings](#Strings)
    3. [DateTime](#DateTime)

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
        6. [aValid](#predicate-avalid)
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

This data profile can be found in the [example folder](../examples/user-account).

Every profile declares some **fields** and some **constraints**.

## Fields

Fields are the "slots" of data that can take values. If generating into a CSV or database, fields are columns. If generating into JSON, fields are object properties. Typical fields might be _email_address_ or _user_id_.

By default, any piece of data is valid for a field. To get more specific, we need to add **rules**.

## Rules

Every rule is a named collection of *constraints*, which can be any of:

* [Predicate constraints](#Predicate-constraints), which limit a field's possible values
* [Presentational constraints](#Presentational-Constraints), which control how values are serialised (eg, number of significant figures)
* [Grammatical constraints](#Grammatical-Constraints), which combine other constraints together

(Predicate and formatting constraints are collectively referred to as **data constraints**)

The decision of how to group constraints into rules is up to the user. At the extremes, there could be a separate rule for each constraint, or one rule containing every constraint. More usually, rules will represent collections of related constraints (eg, _"X is a non-null integer between 0 and 100"_ is a fine rule, comprising four constraints). How to group into rules becomes particularly important when [deliberate violation](../generator/docs/DeliberateViolation.md) is involved.

## Persistence

Profiles are persisted as JSON documents following a [schema](Schema.md) saved as UTF-8.

## Creation

Profiles can be created by any of:

- Writing JSON profiles by hand or by some custom transform process
- ~~Deriving them by supplying some sample data to the Profiler~~ (not yet)
- ~~Designing them through the web front end~~ (not yet)

# Data Types

DataHelix currently recognises four data types: _string_, _datetime_, _integer_ and _decimal_. Keeping this set small is a deliberate goal; it would be possible to have types like _FirstName_ or _Postcode_, but instead these are considered specialisations of the _String_ type, so they can be constrained by the normal string operators (e.g. a user could generate all first names shorter than 10 characters, starting with a vowel).

## Integer/Decimal

Within a profile, users can specify two numeric data types: integer and decimal. Under the hood both of these data types are considered numeric from a point of generation but the integer type enforces a granularity of 1, see below for more information on granularity.

In principle, a decimal can be any real number. In practice, this is any number that can be represented in a Java [BigDecimal](https://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html) object.

In profile files, numbers must be expressed as JSON numbers, without quotation marks.

### Numeric granularity

The granularity of a numeric field is a measure of how small the distinctions in that field can be; it is the smallest positive number of which every valid value is a multiple. For instance:

- if a numeric field has a granularity of 1, it can only be satisfied with multiples of 1; the integer data type adds this constraint by default
- if a decimal field has a granularity of 0.1, it can be satisfied by (for example) 1, 2, 1.1 or 1.2, but not 1.11 or 1.12

Granularities must be powers of ten less than or equal to one (1, 0.1, 0.01, etc). Note that it is possible to specify these granularities in scientific format eg 1E-10, 1E-15 where the _10_ and _15_ clearly distinguish that these numbers can have up to _10_ or _15_ decimal places respectively. Granularities outside these restrictions could be potentially useful (e.g. a granularity of 2 would permit only even numbers) but are not currently supported. 

Decimal fields currently default to the maximum granularity of 1E-20 (0.00000000000000000001) which means that numbers can be produced with up to 20 decimal places. This numeric granularity also dictates the smallest possible step between two numeric values, for example the next biggest decimal than _10_ is _10.00000000000000000001_. A user is able to add a granularTo constraint for a decimal value with coarser granularity (1, 0.1, 0.01...1E-18, 1E-19) but no finer granularity than 1E-20 is allowed

Note that granularity concerns which values are valid, not how they're presented. If the goal is to enforce a certain number of decimal places in text output, the `formattedAs` operator is required. See: [What's the difference between formattedAs and granularTo?](./FrequentlyAskedQuestions.md#whats-the-difference-between-formattedas-and-granularto)

## Strings

Strings are sequences of unicode characters. Currently, only characters from the [Basic Multilingual Plane](https://en.wikipedia.org/wiki/Plane_(Unicode)#Basic_Multilingual_Plane) (Plane 0) are supported.

## DateTime

DateTimes represent specific moments in time, and are specified in profiles through specialised objects:

```javascript
{ "date": "2000-01-01T09:00:00.000" }
```

The format is a subset of [ISO-8601](https://en.wikipedia.org/wiki/ISO_8601); the date and time must be fully specified as above, 
with precisely 3 digits of sub-second precision, plus an optional offset specifier of "Z". All datetimes are treated as UTC.

DateTimes can be in the range `0001-01-01T00:00:00.000Z` to `9999-12-31T23:59:59.999Z`
that is **_`midnight on the 1st January 0001`_** to **_`1 millisecond to midnight on the 31 December 9999`_**

### DateTime granularity

DateTimes currently have granularities derived from the size of their range. Future work ([#141](https://github.com/ScottLogic/datahelix/issues/141)) will make this configurable.


# Predicate constraints

## Theory

* A **predicate constraint** defines any given value as being _valid_ or _invalid_
* The **universal set** contains all generatable values (`null`, any string, any date, any number, etc)
* The **denotation** of a constraint is the subset of the universal set that it defines as valid

See [set restriction and generation](/generator/docs/SetRestrictionAndGeneration.md) for an indepth explanation of how the constraints are merged and data generated from them.

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

Is satisfied if `field` is of type represented by `value` (valid options: `decimal`, `integer`, `string`, `datetime`)

## Textual constraints

<div id="predicate-matchingregex"></div>

### `matchingRegex` _(field, value)_

```javascript
{ "field": "name", "is": "matchingRegex", "value": "[a-z]{0, 10}" }
```

Is satisfied if `field` is a string matching the regular expression expressed in `value`. The regular expression must match the entire string in `field`, start and end anchors `^` & `$` are ignored.

<div id="predicate-containingregex"></div>

### `containingRegex` _(field, value)_

```javascript
{ "field": "name", "is": "containingRegex", "value": "[a-z]{0, 10}" }
```

Is satisfied if `field` is a string containing the regular expression expressed in `value`. Using both start and end anchors `^` & `$` make the constraint behave like `matchingRegex`.

<div id="predicate-oflength"></div>

### `ofLength` _(field, value)_

```javascript
{ "field": "name", "is": "ofLength", "value": 5 }
```

Is satisfied if `field` is a string whose length exactly matches `value`, must be a whole number.

<div id="predicate-longerthan"></div>

### `longerThan` _(field, value)_

```javascript
{ "field": "name", "is": "longerThan", "value": 3 }
```

Is satisfied if `field` is a string with length greater than `value`, must be a whole number.

<div id="predicate-shorterthan"></div>

### `shorterThan` _(field, value)_

```javascript
{ "field": "name", "is": "shorterThan", "value": 3 }
```

Is satisfied if `field` is a string with length less than `value`, must be a whole number.   

<div id="predicate-avalid"></div>

### `aValid` _(field, value)_

```javascript
{ "field": "name", "is": "aValid", "value": "ISIN" }
```

Is satisfied if `field` is a valid `value`, in this case a valid ISIN code. Possible options for `value` are:
* ISIN

**NOTE**: This constraint cannot be combined with any other textual constraint, doing so will mean no string data is created. See [Frequently asked questions](FrequentlyAskedQuestions.md) for more detail.

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


# Grammatical constraints

**Grammatical constraints** combine or modify other constraints. They are fully recursive; any grammatical constraint is a valid input to any other grammatical constraint.

See [set restriction and generation](./../generator/docs/SetRestrictionAndGeneration.md) for an indepth explanation of how the constraints are merged and data generated from them.

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

### `formattedAs` _(field, value)_

```javascript
{ "field": "price", "is": "formattedAs", "value": "%.5s" }
```

Used by output serialisers where string output is required. `value` must be:

* a string recognised by Java's `String.format` method
* appropriate for the data type of `field`
* not `null` (formatting will not be applied for null values)

See the [FAQ](FrequentlyAskedQuestions.md) for the difference between this and [granularTo](#predicate-granularto).

# Profile Validation

The [JSON schema](https://json-schema.org/) for the DataHelix data profile is stored in the file [`datahelix.schema.json`](../schemas/src/main/resources/profileschema/0.1/datahelix.schema.json) in the [schemas module](../schemas/src/main/resources/profileschema/0.1/) directory.

The grammar for the schema is documented in [BNF](https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form) form in the file [`datahelix.profile.bnf`](../schemas/src/main/resources/profileschema/0.1/datahelix.profile.bnf) and in syntax diagrams in the file [ProfileGrammar.md](ProfileGrammar.md)

## JetBrains IntelliJ

To use the DataHelix profile JSON schema in IntelliJ we need to  set up the intellij editor to validate all json files under the `json` and/or `examples` directories against the `datahelix.schema.json` schema file.

To setup IntelliJ to validate json files against the schema follow these steps:

1. Open IntelliJ
1. Select `File` -> `Settings` -> `Languages & Frameworks` -> `Schemas and DTDs'
1. Select `JSON Schema Mappings`
1. Press the `+` button to add a new schema mapping
1. Give the mapping a name (e.g. `DataHelix Profile Schema`)
1. For `Schema file or URL:` select the local schema file (e.g. `<project root>/datahelix/json/datahelix.schema.json`)
1. Make sure the `Schema version:` is set to `JSON schema version 7`
1. Press the `+` button to add a new mapping
1. Select `Add Directory`
1. Select the `json` directory
1. Press okay

Now when you open a json file from the `json` directory in IntelliJ, it will be automatically validated against the DataHelix profile schema.


## Microsoft Visual Studio Code

**_Although Visual Studio Code appears to validate the profile json files against the schema, it incorrectly shows some profile files as valid when there are syntactic errors in them._**

**_For this reason the use of Visual Stuido Code is not recommended for DataHelix development._**

To enable visual studio code to validate json files against the DataHelix profile schema a `json.schemas` section needs to be added to the `settings.json` file.

To do this:

1. Click on the gear icon (<img src="../wikiimages/settingsicon.png" width="16" height="16">) at the bottom left of the screen and select `Settings`
1. In the settings windows, click `Extensions` -> `JSON`
1. You should see a section like this:
    ```
    Schemas
    Associate schemas to JSON files in the current project
    Edit in settings.json
    ```
1. Click on the `Edit in settings.json` link and VSCode will open the settings.json file.
1. Add the following snippet to the end of the file (replacing `<datahelix_projectroot>` with the root directory path for the DataHelix project and replacing the `"fileWatch"` value with an appropriate value for your configuration):
    ```
      "json.schemas": [
        {
          "fileMatch": [
            "<datahelix_projectroot>/*"
          ],
          "url": "file:///<datahelix_projectroot>/schemas/src/main/resources/profileschema/0.1/datahelix.schema.json"
        }
      ]
    ```
    to verify that the url to the `datahelix.schema.json` is valid you can `ctrl-click` on it and the schema file will open in the editor.  
1. If the ` "json.schemas"` snippet already exists, you can add a new object to the JSON array for the DataHelix profile schema.


## Schema Validation using library

To validate a DataHelix Profile json file against the schema the `schema.jar` file needs to be included in the project:

To include the schema.jar file in a maven project add the following dependency: 
```
<dependencies>
    <dependency>
        <groupId>com.scottlogic.deg</groupId>
        <artifactId>schemas</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    .
    .
    .
</dependencies>
```

Then create an instance of the [`ProfileValidator`](https://github.com/ScottLogic/datahelix/blob/master/schemas/src/main/java/com/scottlogic/deg/schemas/v0_1/ProfileValidator.java) object and call the validateProfile() method passing in an `java.io.InputStream` that contains the profile data to be validated.

An example of calling the validator:

```java
File profileFile = new File("path/to/profile.json");
InputStream profileStream = new FileInputStream(profileFile);
ValidationResult result = profileValidator.validateProfile(profileStream);
```
This will return a [`ValidationResult`](https://github.com/ScottLogic/datahelix/blob/master/schemas/src/main/java/com/scottlogic/deg/schemas/common/ValidationResult.java) object which contains a list of error messages found during validation.

If the list of error messages is empty then validation was successful. `ValidationResult.isValid()` is a convenience method that can be used to check whether validation was successful.

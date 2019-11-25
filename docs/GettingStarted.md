# Getting Started

The following guide gives a 10 minute introduction to the generator via various practical examples.

# Contents

  - [Getting Started](#Getting-started)
  - [Downloading the JAR file](#Downloading-the-JAR-file)
  - [Creating your first profile](#Creating-your-first-profile)
  - [Running the generator](#Running-the-generator)
  - [Adding constraints](#Adding-constraints)
  - [Generating large datasets](#Generating-large-datasets)
  - [Data types](#Data-types)
  - [Generation modes](#Generation-modes)
  - [Next steps](#Next-steps)

## Downloading the JAR file

The generator is distributed as a JAR file, with the latest release always available from the [GitHub releases page](https://github.com/finos/datahelix/releases/). The project is currently in beta and under active development. You can expect breaking changes in future releases, and new features too!

 You will need Java v1.8 installed to run the generator (you can run `java -version` to check whether you meet this requirement), it can be [downloaded here](https://www.java.com/en/download/manual.jsp).

You are also welcome to download the source code and build the generator yourself. To do so, follow the instructions in the [Developer Guide](DeveloperGuide.md#Building).

Your feedback on the beta would be greatly appreciated. If you have any issues, feature requests, or ideas, please share them via the [GitHub issues page](https://github.com/finos/datahelix/issues).

## Creating your first profile

Profiles are JSON files that describe the data you want to generate. They are composed of:

-   **fields** - an array of uniquely named fields (or columns).
-   **constraints** - an array of restrictions on the types and ranges of data permitted for the given column.

We'll start by generating data for a trivial schema. Using [VS Code](https://code.visualstudio.com/) or your favourite text editor, create the following JSON profile and save it as `profile.json`:

```json
{
    "fields": [{ "name": "firstName", "type": "string" }],
    "constraints": []
}
```

When manually writing profiles, we recommend using a text editor which can validate profiles using the datahelix schema. Instructions for how to setup automatic profile validation using VS Code can be found [here](user/ProfileValidation.md).

## Running the generator

Now place the `generator.jar` file (downloaded from the [GitHub releases page](https://github.com/finos/datahelix/releases/)) in the same folder as the profile, open up a terminal, and execute the following:

```
$ java -jar generator.jar generate --max-rows=100 --replace --profile-file=profile.json --output-path=output.csv
```

The generator is a command line tool which reads a profile, and outputs data in CSV or JSON format. The `--max-rows=100` option tells the generator to create 100 rows of data, and the `--replace` option tells it to overwrite previously generated files. The compulsary `--profile-file` option specifies the name of the input profile, and the `--output-path` option specifies the location to write the output to. In `generate` mode `--output-path` is optional; the generator will default to standard output if it is not supplied. By default the generator outputs progress, in rows per second, to the standard error output. This can be useful when generating large volumes of data.

If you open up `output.csv` you'll see something like the following:

```
firstName
"$N!R"
"_$"

"n"


"y"
[...]
```

The generator has successfully created 100 rows of random data. However, in most cases this data will not be terribly useful. It is likely that `firstName` should only allow a subset of possible string values. If you don't provide any constraints, the generator will output random strings containing basic latin characters and punctuation.

Let's assume you only want to generate characters between a to z for the `firstName` field; this can be achieved by adding a `matchingRegex` constraint for the field. With this constraint alone, the generator will only output strings valid for the regex.

## Adding constraints

Update the JSON profile as follows:

```json
{
    "fields": [{ "name": "firstName", "type": "string" }],
    "constraints": [{ "field": "firstName", "matchingRegex": "[a-z]{1,10}" }]
}
```

Re-running generation now creates a file containing random strings that match the simple regex `[a-z]{1,10}`:

```
firstName
"f"

"draxonsa"
"j"
"srsub"

"f"

[...]
```

The generator supports two different types of constraint:

-   [**Predicates**](GettingStarted.md#Data-Types) - boolean-valued functions that define whether a given value is valid or invalid. In the above profile `ofType` and `matchingRegex` are examples of predicates.
-   [**Grammatical**](GettingStarted.md#Data-Types) - combine or modify other constraints including other grammatical constraints. In the above profile `not` is an example of a grammatical constraint.

The current profile outputs random text strings for the `firstName` field. Depending on what you are intending to use this data for this may or may not be appropriate. For testing purposes, you are likely to want output data that has a lot of variability, however, if you are using the generator to create simulation data, you might want to use a regex that creates more realistic data, for example the regex `(Joh?n|Mar[yk])`.

## Generating large datasets

The generator has been designed to be fast and efficient, allowing you to generate large quantities of test and simulation data. If you supply a large number for the `--max-rows` option, the data will be streamed to the output file, with the progress / velocity reported during generation.

```
$ java -jar generator.jar generate --max-rows=10000 --replace --profile-file=profile.json --output-path=output.csv
Generation started at: 16:41:44

Number of rows | Velocity (rows/sec) | Velocity trend
---------------+---------------------+---------------
1477           | 1477                | +
4750           | 3271                | +
9348           | 4597                | +
10000          | 0                   | -
```

If the generation is taking too long, you can halt the command via <kbd>Ctrl</kbd>+<kbd>C</kbd>

## Data types

The generator supports several different data types including:

-   **integer** - any integer number between -1E20 and 1E20 inclusive
-   **decimal** - any real number between -1E20 and 1E20 inclusive, with an optional granularity / precision (a power of ten between 1 and 1E-20) that can be defined via a `granularTo` constraint.
-   **string** - sequences of unicode characters up to a maximum length of 1000 characters
-   **datetime** - specific moments in time, with values in the range 0001-01-01T00:00:00.000 to 9999-12-31T23:59:59.999, with an optional granularity / precision (from a maximum of one year to a minimum of one millisecond) that can be defined via a `granularTo` constraint.

A full list of the supported data types can be found in the [user guide](UserGuide.md#Data-Types).

We'll expand the example profile to add a new `age` field, a not-null integer in the range 1-99:

```json
{
    "fields": [
        { "name": "firstName", "type": "string" },
        { "name": "age", "type": "integer" }
    ],
    "constraints": [
        { "field": "firstName", "matchingRegex": "(Joh?n|Mar[yk])" },
        { "field": "age", "greaterThan": 0 },
        { "field": "age", "lessThan": 100 }
    ]
}
```

With the change in the `firstname` regex, the output is now starting to look quite realistic:

```
firstName,age
John,58
Jon,70
John,93
Mary,89
Jon,65
Jon,33
Mark,94
Jon,76
John,30
[...]
```

Finally, we'll add a field for National Insurance number. In this case, the constraints applied to the field ensure that it only has a value if the age is greater than or equal to 16:

```json
{
    "fields": [
        { "name": "firstName", "type": "string" },
        { "name": "age", "type": "integer" },
        { "name": "nationalInsurance", "type": "string", "nullable": true }
    ],
    "constraints": [
        { "field": "firstName", "matchingRegex": "(Joh?n|Mar[yk])" },
        { "field": "age", "greaterThan": 0 },
        { "field": "age", "lessThan": 100 },
        {
            "if": {
                "field": "age",
                "greaterThanOrEqualTo": 16
            },
            "then": {
                "allOf": [
                    {
                        "field": "nationalInsurance",
                        "matchingRegex": "[A-CEGHJ-PR-TW-Z]{1}[A-CEGHJ-NPR-TW-Z]{1}[0-9]{6}[A-DFM]{0,1}"
                    },
                    {
                        "field": "nationalInsurance",
                        "isNull": false
                    }
                ]
            },
            "else": {
                "field": "nationalInsurance",
                "isNull": true
            }
        }
    ]
}
```

The above profile yields the following output:

```
firstName,age,nationalInsurance
"Mary",85,"RP139559B"
"Jon",2,
"Mark",10,
"Mary",20,"NW372471"
"Mark",47,"ZP195766"
"Jon",41,"RW557276F"
"Mark",16,
"Jon",69,"OR650886"
"John",19,"CB460473"
"Mark",55,"AE656210B"
"Mark",68,"ER027015"
[...]
```

Fields are non-nullable by default, however, you can indicate that a field is nullable:

```json
{
    "name": "field1",
    "type": "datetime",
    "nullable": true
}
```

You can find out more about the various constraints the generator supports in the detailed [User Guide](UserGuide.md).

## Generation modes

The generator supports two different generation modes:

-   **random** - _(default)_ generates random data that abides by the given set of constraints, with the number of generated rows limited via the `--max-rows` option.
-   **full** - generates all the data that abides by the given set of constraints, with the number of generated rows limited via the `--max-rows` option.

The mode is specified via the `--generation-type` option.

## Next steps

* If you'd like to find out more about the various constraints the tool supports, the [User Guide](UserGuide.md) is a good next step.

* You might also be interested in the [examples folder](../examples), which illustrates various features of the generator.

* Checkout some [FAQs](user/FrequentlyAskedQuestions.md) about datahelix.

* For a more in-depth technical insight, see the [Developer Guide](DeveloperGuide.md).

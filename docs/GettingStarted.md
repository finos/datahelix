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

# Getting Started

The following guide gives a 10 minute introduction to the generator via various practical examples.

* For more detailed documentation please refer to the [User Guide](UserGuide.md)

* If you are interested in extending / modifying the generator itself please refer to the [Developer Guide](DeveloperGuide.md)

## Downloading the JAR file

The generator has been written in Java, allowing it to work on Microsoft Windows, Apple Mac and Linux. You will need Java v1.8 installed to run the generator (you can run `java -version` to check whether you meet this requirement), it can be [downloaded here](https://www.java.com/en/download/manual.jsp).

The generator is distributed as a JAR file, with the latest release always available from the [GitHub releases page](https://github.com/finos/datahelix/releases/). The project is currently in beta and under active development. You can expect breaking changes in future releases, and new features too!

You are also welcome to download the source code and build the generator yourself. To do so, follow the instructions in the [Developer Guide](DeveloperGuide.md).

Your feedback on the beta would be greatly appreciated. If you have any issues, feature requests, or ideas, please share them via the [GitHub issues page](https://github.com/finos/datahelix/issues).

## Creating your first profile

Profiles are JSON files that describe the data you want to generate. They are composed of:

-   **fields** - an array of uniquely named fields (or columns).
-   **rules** - an array of constraints, with an optional description.
-   **constraints** - restrict the types and ranges of data permitted for the given column.

We'll start by generating data for a trivial schema. Using your favourite text editor, create the following JSON profile and save it as `profile.json`:

```json
{
    "schemaVersion": "0.7",
    "fields": [{ "name": "firstName", "type": "string" }],
    "rules": []
}
```
### Profile Validation

The [JSON schema](https://json-schema.org/) for the DataHelix data profile is stored in the file `datahelix.schema.json` in the [schemas](https://github.com/finos/datahelix/tree/master/profile/src/main/resources/profileschema) directory.

We recommend using Visual Studio Code to validate your profiles. To enable it to validate json files against the DataHelix profile schema a `json.schemas` section needs to be added to the `settings.json` file.

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
          "url": "file:///<datahelix_projectroot>/profile/src/main/resources/profileschema/datahelix.schema.json"
        }
      ]
    ```
    Alternatively you can configure this to any naming convention you want for profile files, for example `"*.profile.json"`.

To verify that the url to the `datahelix.schema.json` is valid you can `ctrl-click` on it and the schema file will open in the editor. If the ` "json.schemas"` snippet already exists, you can add a new object to the JSON array for the DataHelix profile schema.

## Running the generator

Now place the `generator.jar` file (downloaded from the [GitHub releases page](https://github.com/finos/datahelix/releases/)) in the same folder as the profile, open up a terminal, and execute the following:

```
$ java -jar generator.jar generate --max-rows=100 --replace --profile-file=profile.json --output-path=output.csv
```

The generator is a command line tool which reads a profile, and outputs data in CSV format. The `--max-rows=100` option tells the generator to create 100 rows of data, and the `--replace` option tells it to overwrite previously generated files. The compulsary `--profile-file` option specifies the name of the input profile, and the `--output-path` option specifies the location to write the output to. In `generate` mode `--output-path` is optional; the generator will default to standard output if it is not supplied. By default the generator outputs progress, in rows per second, to the standard error output. This can be useful when generating large volumes of data.

If you open up `output.csv` you'll see something like the following:

```
firstName
"$N!R"
"_$"

"n"


"y"
[...]
```

The generator has successfully created 100 rows of random data. However, in most cases this data will not be terribly useful. It is likely that `firstName` will only allow a subset of possible string values. If you don't provide any constraints, the generator will output random strings containing basic latin characters and punctuation.

Let's assume you only want to generate characters between a to z for the `firstName` field; this can be achieved by adding a `matchingRegex` constraint for the field. With this constraint alone, the generator will only output strings valid for the regex.

## Adding constraints

Update the JSON profile as follows:

```json
{
    "schemaVersion": "0.7",
    "fields": [{ "name": "firstName", "type": "string" }],
    "rules": [
        {
            "rule": "first name constraints",
            "constraints": [
                {
                    "field": "firstName",
                    "is": "matchingRegex",
                    "value": "[a-z]{1,10}"
                }
            ]
        }
    ]
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

Fields are nullable by default, however, you can add a further constraint to a field to indicate it is 'not null'. If you add the following constraint, the generator will no longer output null values:

```json
"constraints": [
  { "field": "firstName", "is": "matchingRegex", "value": "[a-z]{1,10}" },
  { "not": { "field": "firstName", "is": "null" } }
]
```

The generator supports three different types of constraint. These are:

-   **Predicates** - boolean-valued functions that define whether a given value is valid or invalid. In the above profile `ofType` and `matchingRegex` are examples of predicates.
-   **Grammatical** - combine or modify other constraints. They are fully recursive; any grammatical constraint is a valid input to any other grammatical constraint. In the above profile `not` is an example of a grammatical constraint.
-   **Presentational** - provide additional formatting information, we'll cover these later.

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

The generator supports four different data types:

-   **integer** - any integer number between -1E20 and 1E20 inclusive
-   **decimal** - any real number between -1E20 and 1E20 inclusive, with an optional granularity / precision (a power of ten between 1 and 1E-20) that can be defined via a `granularTo` constraint.
-   **string** - sequences of unicode characters up to a maximum length of 1000 characters
-   **datetime** - specific moments in time, with values in the range 0001-01-01T00:00:00.000 to 9999-12-31T23:59:59.999, with an optional granularity / precision (from a maximum of one year to a minimum of one millisecond) that can be defined via a `granularTo` constraint.

We'll expand the example profile to add a new `age` field, a not-null integer in the range 1-99:

```json
{
    "schemaVersion": "0.7",
    "fields": [
        { "name": "firstName", "type": "string" }, 
        { "name": "age", "type": "integer"  }
    ],
    "rules": [
        {
            "constraints": [
                { "not": { "field": "firstName", "is": "null" } },
                {
                    "field": "firstName",
                    "is": "matchingRegex",
                    "value": "(Joh?n|Mar[yk])"
                }
            ]
        },
        {
            "constraints": [
                { "field": "age", "is": "greaterThan", "value": 0 },
                { "field": "age", "is": "lessThan", "value": 100 },
                { "not": { "field": "age", "is": "null" } }
            ]
        }
    ]
}
```

With the change in the `firstname` regex, the output is now starting to look quite realistic:

```
firstName,age
"John",31
"Mary",73
"Jon",44
"John",24
"Jon",43
"Jon",18
"Jon",5
"John",82
"John",19
"Jon",43
[...]
```

Finally, we'll add a field for National Insurance number. In this case, the constraints applied to the field ensure that it only has a value if the age is greater than or equal to 16:

```json
{
    "schemaVersion": "0.7",
    "fields": [
        { "name": "firstName", "type": "string" }, 
        { "name": "age", "type": "integer"  },
        { "name": "nationalInsurance", "type": "string"  }
    ],
    "rules": [
        {
            "rule": "first name",
            "constraints": [
                { "not": { "field": "firstName", "is": "null" } },
                {
                    "field": "firstName",
                    "is": "matchingRegex",
                    "value": "(Joh?n|Mar[yk])"
                }
            ]
        },
        {
            "rule": "age",
            "constraints": [
                { "field": "age", "is": "greaterThan", "value": 0 },
                { "field": "age", "is": "lessThan", "value": 100 },
                { "not": { "field": "age", "is": "null" } }
            ]
        },
        {
            "rule": "national insurance",
            "constraints": [
                {
                    "if": {
                        "field": "age",
                        "is": "greaterThanOrEqualTo",
                        "value": 16
                    },
                    "then": {
                        "allOf": [
                            {
                                "field": "nationalInsurance",
                                "is": "matchingRegex",
                                "value": "[A-CEGHJ-PR-TW-Z]{1}[A-CEGHJ-NPR-TW-Z]{1}[0-9]{6}[A-DFM]{0,1}"
                            },
                            {
                                "not": {
                                    "field": "nationalInsurance",
                                    "is": "null"
                                }
                            }
                        ]
                    },
                    "else": {
                        "field": "nationalInsurance",
                        "is": "null"
                    }
                }
            ]
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

You can find out more about the various constraints the generator supports in the detailed [User Guide](UserGuide.md).

## Generation modes

The generator supports a number of different generation modes:

-   **random** - _(default)_ generates random data that abides by the given set of constraints, with the number of generated rows limited via the `--max-rows` option.
-   **full** - generates all the data that abides by the given set of constraints, with the number of generated rows limited via the `--max-rows` option.

The mode is specified via the `--generation-type` option.

## Next steps

That's the end of our getting started guide. Hopefully it has given you a good understanding of what the DataHelix generator is capable of. 

* If you'd like to find out more about the various constraints the tool supports, the [User Guide](UserGuide.md) is a good next step. 

* You might also be interested in the [examples folder](https://github.com/finos/datahelix/tree/master/examples), which illustrates various features of the generator.

* For a more in-depth technical insight, see the [Developer Guide](DeveloperGuide.md).

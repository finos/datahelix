# DataHelix Generator

[![CircleCI](https://circleci.com/gh/ScottLogic/datahelix.svg?style=svg)](https://circleci.com/gh/ScottLogic/datahelix)

The generation of representative test and simulation data is a challenging and time-consuming task. The DataHelix generator allows you to quickly create data, based on a JSON profile that defines fields and the relationships between them, for the purpose of testing and validation. The generator supports a number of generation modes, allowing the creation of data that both conforms to, or violates, the profile.

  - [Getting Started](#getting-started)
    - [Creating your first profile](#creating-your-first-profile)
    - [Adding constraints](#adding-constraints)
    - [Generating large datasets](#generating-large-datasets)
    - [Data types](#data-types)
    - [Generation modes](#generation-modes)
    - [Generating invalid data](#generating-invalid-data)
    - [Next steps](#next-steps)

# Getting Started

*The following guide gives a 10 minute introduction to the generator via various practical examples. For more detailed documentation please refer to the [Profile Developer Guide](docs/ProfileDeveloperGuide.md), and if you are interested in extending / modifying the generator itself, refer to the [DataHelix Generator Developer Guide](docs/GeneratorDeveloperGuide.md).*

The generator has been written in Java, allowing it to work on Microsoft Windows, Apple Mac and Linux. You will need Java v1.8 installed to run the generator (you can run `java version` to check whether you meet ths requirement), it can be [downloaded here](https://www.java.com/en/download/manual.jsp).

The generator is distributed as a JAR file, with the latest release always available from the [GitHub releases page](https://github.com/ScottLogic/datahelix/releases/). The project is currently in beta and under active development. You can expect breaking changes in future releases, and new features too!

Your feedback on the beta would be greatly appreciated. If you have any issues, feature requests, or ideas, please share them via the [GitHub issues page](https://github.com/ScottLogic/datahelix/issues).

## Creating your first profile

Profiles are JSON files that describe the data you want to generate. They are composed of:

 - **fields** -  an array of uniquely named fields (or columns).
 - **rules** - an array of constraints, with an optional description.
 - **constraints** - restrict the types and ranges of data permitted for the given column.

We'll start by generating data for a trivial schema. Using your favourite text editor, create the following JSON profile and save it as `profile.json`:

~~~json
{
  "schemaVersion": "0.1",
  "fields": [
    { "name": "firstName" }
  ],
  "rules": []
}
~~~

Now place the `generator.jar` file (downloaded from the [GitHub releases page](https://github.com/ScottLogic/datahelix/releases/) in the same folder as the profile, open up a terminal, and execute the following:

~~~
$ java -jar generator.jar generate --max-rows=100 --replace profile.json output.csv
Generation started at: 08:02:52

Number of rows | Velocity (rows/sec) | Velocity trend
---------------+---------------------+---------------
100            | 0                   | - 
~~~

<!-- bug velocity not right -->

The generator is a command line tool which reads a profile, and outputs data in CSV format. The `--max-rows=100` option tells the generator to create 100 rows of data, and the `--replace` option tells it to overwrite previously generated files. The generator outputs progress, in rows per second, which is useful when generating large volumes of data.

If you open up `output.csv` you'll see the following:

~~~
firstName
15-09-1971 04:30:18
28-10-2087 09:23:30
1876153676
334227637

03-04-2053 04:57:06
"Lorem Ipsum"
[...]
~~~

The generator has successfully created 100 rows of random data. However, in most cases this data will not be terribly useful. It is likely that `firstName` will only allow string values, with this constraint enforced via a database schema for example. If you don't provide any constraints, the generator will output values from the 'universal set', which  contains all generatable values (null, any string, any date, any number, etc).

Let's assume you only want to generate string values for the `firstName` field; this can be achieved by adding an `ofType` constraint for the field. With this constraint alone, the generator will output random strings containing characters from the unicode [Basic Multilingual Plane](https://en.wikipedia.org/wiki/Plane_(Unicode)#Basic_Multilingual_Plane). If you want a more restrictive character set, ths can be achieved by adding `matchingRegex`. 


## Adding constraints

Update the JSON profile as follows:

~~~json
{
  "schemaVersion": "0.1",
  "fields": [
    { "name": "firstName" }
  ],
  "rules": [
    {
      "rule": "first name constraints",
      "constraints": [
        { "field": "firstName", "is": "ofType", "value": "string" },
        { "field": "firstName", "is": "matchingRegex", "value": "[a-z]{1,10}" }
      ]
    }
  ]
}
~~~

Re-running generation now creates a file containing random strings that match the simple regex `[a-z]{1,10}`:

~~~
firstName
"f"

"draxonsa"
"j"
"srsub"

"f"

[...]
~~~

Fields are nullable by default, however, you can add a further constraint to a field to indicate it is 'not null'. If you add the following constraint, the generator will no longer output null values:

~~~json
"constraints": [
  { "field": "firstName", "is": "ofType", "value": "string" },
  { "field": "firstName", "is": "matchingRegex", "value": "[a-z]{1,10}" },
  { "not": { "field": "firstName", "is": "null" } }
]
~~~

The generator supports three different types of constraint. These are:

 - **Predicates** - boolean-valued functions that define whether a given value as valid or invalid. In the above profile `ofType` and `matchingRegex` are examples of predicates.
 - **Grammatical** - combine or modify other constraints. They are fully recursive; any grammatical constraint is a valid input to any other grammatical constraint. In the above profile `not` is an example of a grammatical constraint. 
 - **Presentational** - provide additional formatting information, we'll cover these later.

The current profile outputs random text strings for the `firstName` field. Depending on what you are intending to use this data for this may or may not be appropriate. For testing purposes, you are likely to want output data that has a lot of variability, however, if you are using the generator to create simulation data, you might want to use a regex that creates more realistic data, for example the regex `(Joh?n|Mar[yk])`.

## Generating large datasets

The generator has been designed to be fast and efficient, allowing you to generate large quantities of test and simulation data. If you supply a large number for the `--max-rows` option, the data will be streamed to the output file, with the progress / velocity reported during generation.

```
$ java -jar generator.jar generate --max-rows=10000 --overwrite profile.json output.csv
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

 - **integer** - any integer that can be defined by the Java [BigDecimal](https://docs.oracle.com/javase/7/docs/api/java/math/BigDecimal.html) type
 - **decimal** - any real number (again a BigDecimal), with an optional granularity / precision that can be defined via a `granularTo` constraint.
 - **string** - sequences of unicode characters
 - **datetime** - specific moments in time, with values in the range 0001-01-01T00:00 to 9999-12-31T23:59

<!-- TODO: rename as datetime -->

We'll expand the example profile to add a new `age` field, a not-null integer in the range 1-99:

~~~json
{
  "schemaVersion": "0.1",
  "fields": [
    { "name": "firstName" },
    { "name": "age" }
  ],
  "rules": [
    {
      "constraints": [
        { "field": "firstName", "is": "ofType", "value": "string" },
        { "not": { "field": "firstName", "is": "null" } },
        { "field": "firstName", "is": "matchingRegex", "value": "(Joh?n|Mar[yk])" }
      ]
    },
    {
      "constraints": [
        { "field": "age", "is": "ofType", "value": "integer" },
        { "field": "age", "is": "greaterThan", "value": 0 },
        { "field": "age", "is": "lessThan", "value": 100 },
        { "not": { "field": "age", "is": "null" } }
      ]
    }
  ]
}
~~~

With the change in the `firstname` regex, the output is now starting to look quite realistic:

~~~
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
~~~

Finally, before exploring some more interesting features of the generator, we'll add a field for National Insurance number. In this case, the constraints applied to the field ensure that it only has a value if the age is greater than or equal to 16:

~~~json
{
  "schemaVersion": "0.1",
  "fields": [
    { "name": "firstName" },
    { "name": "age" },
    { "name": "nationalInsurance" }
  ],
  "rules": [
    {
      "rule": "first name",
      "constraints": [
        { "field": "firstName", "is": "ofType", "value": "string" },
        { "not": { "field": "firstName", "is": "null" } },
        { "field": "firstName", "is": "matchingRegex", "value": "(Joh?n|Mar[yk])" }
      ]
    },
    {
      "rule": "age",
      "constraints": [
        { "field": "age", "is": "ofType", "value": "numeric" },
        { "field": "age", "is": "greaterThan", "value": 0 },
        { "field": "age", "is": "lessThan", "value": 100 },
        { "not": { "field": "age", "is": "null" } }
      ]
    },
    {
      "rule": "national insurance",
      "constraints": [
        { "field": "nationalInsurance", "is": "ofType", "value": "string" },
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
~~~

The above profile yields the following output:

~~~
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
~~~

You can find out more about the various constraints the generator supports in the detailed [Profile Developer Guide](docs/ProfileDeveloperGuide.md).

## Generation modes

The generator supports a number of different generation modes:

 - **random** - generates random data that abides by the given set of constraints, with the number of generated rows limited via the `--max-rows` option.
 - **interesting** - generates data that is typically deemed 'interesting' from a test perspective, for example exploring [boundary values](https://en.wikipedia.org/wiki/Boundary-value_analysis).

The mode is specified via the `--generation-mode` option. The following example outputs 'interesting' values for the current profile:

~~~
$ java -jar generator.jar generate --generation-type interesting --overwrite profile.json output.csv
~~~

In this case it generates just 14 rows where you can see that it is exploring the boundary values of the constraints:

~~~
firstName,age,nationalInsurance
"Jon",18,"AA000000"
"John",18,"AA000000"
"Jon",18,"AJ000000F"
"John",18,"AJ000000F"
"Jon",19,"AA000000"
"John",19,"AA000000"
"Jon",19,"AJ000000F"
"John",19,"AJ000000F"
"Jon",1,
"John",1,
"Jon",99,"AA000000"
"John",99,"AA000000"
"Jon",99,"AJ000000F"
"John",99,"AJ000000F"
~~~

<!-- I've got a few questions about this output! -->

## Generating invalid data

One of the most powerful features of the generator is its ability to generate data that violates constraints. To create invalid data use the `--violate` command line option. This time you need to specify an output directory rather than a file:

~~~
$ java -jar generator.jar generate --violate --max-rows=100 --overwrite profile.json out
~~~

When the above command has finished, you'll find that the generator has created an `out` directory which has four files:

~~~
$ ls out
1.csv           2.csv           3.csv           manifest.json
~~~

The manifest file details which rules have been violated in each of the output files:

~~~
{
  "cases": [
    {
      "filePath": "1",
      "violatedRules": ["first name"]
    },
    {
      "filePath": "2",
      "violatedRules": ["age"]
    },
    {
      "filePath": "3",
      "violatedRules": ["national insurance"]
    }
  ]
}
~~~

If you take a look at the generated file `1.csv` you'll see that data for the `firstName` field does not objy the given constraints, whilst those for `age` and `nationalInsurance` are correct:

~~~
firstName,age,nationalInsurance
-619248029,71,"HT849919"
08-12-2001 02:53:16,15,
"Lorem Ipsum",11,
"Lorem Ipsum",71,"WX004081"
1263483797,19,"HG054666"
,75,"GE023082"
"Lorem Ipsum",59,"ZM850737C"
[...]
~~~

However, it might be a surprise to see nulls, numbers and dates as values for the `firstName` field alongside strings that do not match the regex given in the profile. This is because these are all defined as a single rule within the profile. You have a couple of options if you want to ensure that `firstName` is not null and a string, the first is to inform the generator that it should not violate specific constraint types:

~~~
$ java -jar generator.jar generate --violate --dont-violate=ofType null \
  --max-rows=100 --overwrite profile.json out
~~~

Or, alternatively, you can re-arrange your constraints so that the ones that define types / null, are grouped as a single rule. After By re-grouping constraints, the following output, with random strings that violate the regex constraint, is generated:

```
firstName,age,nationalInsurance
"�",43,"PT530853D"
"뷇",56,"GE797875M"
"邦爃",84,"JA172890M"
"J㠃懇圑㊡俫杈",32,"AE613401F"
"俵튡",38,"TS256211F"
"M",60,"GE987171M"
"M",7,
"Mꞎኅ剭Ꙥ哌톞곒",97,"EN082475C"
")",80,"BX025130C"
",⑁쪝",60,"RW177969"
"5ᢃ풞ﺯ䒿囻",57,"RY904705" 
[...]
```
**NOTE** we are considering adding a feature that allows you to [specify / restrict the character set](https://github.com/ScottLogic/datahelix/issues/294) in a future release.

## Next steps

That's the end of our getting started guide. Hopefully it has given you a good understanding of what the DataHelix generator is capable of. If you'd like to find out more about the various constraints the tool supports, the [Profile Developer Guide](docs/ProfileDeveloperGuide.md) is a good next step. You might also be interested in the [examples folder](https://github.com/ScottLogic/datahelix/tree/master/examples), which illustrates various features of the generator.

## License

Copyright 2019 Scott Logic Ltd

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)

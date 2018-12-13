<!-- Badges go here (see [shields.io](https://shields.io/), for examples). -->

# Data Engineering Generator

This monorepo contains the different technological arms of Scott Logic's Data Generator project, which allows users to automatically analyse their real data to create a profile, and then use that profile to generate realistic dummy data.

There are 3 discrete primary elements:

| Product | Description | Status | Notes |
| ---- | ---- | ---- | ---- |
| Profiler | Analyse a data source and generate a profile | Stable - Pre-alpha | Currently being developed |
| Profile | A representation of the data schema and rules which can be used to generate data | Stable - alpha | |
| Generator | A tool for generating data from a given profile | Stable - alpha | Supports data generation and generation of data that has been [delibrately violated](./generator/docs/DeliberateViolation.md). |

See [overview](./docs/Profiles.md) and [schema](./docs/Schema.md) for details about profiles and how they work.

## Installation

This product requires **Java version 1.8** to be installed. Later versions are not supported at present.
* Clone the repository
* Follow the steps in the [profiler setup instructions](profiler/README.md). The following need to be actioned for the data generator product, the remaining steps need to be actioned to be able to use the profiler.

  | Generator | Profiler |
  | ---- | ---- |
  | JDK | JDK |
  |  | Scala |
  |  | Spark |
  |  | Hadoop |
  |  | Windows shims |
  | Cucumber | Cucumber | 
  | IntelliJ/Eclipse IDE | IntelliJ/Eclipse IDE |
* Build the solution using your preferred method - the project is fully compatible with IntelliJ and Eclipse
* Run the data generator or profiler - see the examples below.

The product is confirmed compatible with Microsoft Windows, and should be compatible with all other operating systems, but this has not been confirmed to date.

## Usage example

<!-- A few motivating and useful examples of how your project can be used. Spice this up with code blocks and potentially screenshots / videos ([LiceCap](https://www.cockos.com/licecap/) is great for this kind of thing) -->

### Profiler
#### Generate a profile
Edit the **run configuration** for App.scala:

In **VM Options**, specify the folder in which the hadoop.dll lives:

`-Djava.library.path="C:\hadoop-2.7.6\bin"`

In **Program Arguments**, specify the path to an input csv for analysis, and an output path to a directory

`"<path to data>" "<path to output directory>"`

When App.scala is run it will produce a text file in the specified output location and print classification results in the terminal

**There is an example CSV file which can be found in:**

`"C:<your-path>\data-engineering-generator\profiler\src\test\resources\gfx_cleaned.csv"`
### Generator

#### Generate valid data
`dg generate "<path to profile>" "<path to output directory or file>"`

Optional additional switches:
* `-n 1000` limit the output to 1000 rows
* `-t <data-generation-type>` - produce different data in different ways, see [generation types](./generator/docs/GenerationTypes.md)

Pass no arguments to `dg generate` to see additional switches and values.

#### Generate violated data
`dg generateTestCases "<path to profile>" "<path to output directory>"`

Optional additional switches:
* `-n 1000` limit the output to 1000 rows per rule violation
* `-t <data-generation-type>` - produce different data in different ways, see [generation types](./generator/docs/GenerationTypes.md)

Pass no arguments to `dg generateTestCases` to see additional switches and values.

## Documentation

* [Data profiles](./docs/Profiles.md)
* [Decision trees](./docs/DecisionTrees/DecisionTrees.md)
* [Generator](./generator/README.md)
  * [Generation algorithm](./generator/docs/GenerationAlgorithm.md)

* [Frequently asked questions](docs/FrequentlyAskedQuestions.md)

Developers of Data Generator, or users who're curious about an aspect of its functionality, might be interested in the [key decisions log](docs/KeyDecisions.md).

## Development setup

1) Follow the [profiler setup instructions](profiler/README.md).
2) Import the top level working directory `data-engineering-generator` into your IDE as an Maven project (In Eclipse: File > Import... > Maven > Existing Maven Project).  Four projects will be imported as a result:
   - `parent`
   - `generator`
   - `profiler`
   - `schemas` 

## Contributing

See [contributing](./.github/CONTRIBUTING.md)

## License

Copyright 2018 Scott Logic Ltd

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)

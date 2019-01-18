<!-- Badges go here (see [shields.io](https://shields.io/), for examples). -->

# Data Generator

### Welcome to the _Scott Logics_ data generator.
The generation of representative test and simulation data is challenging and time-consuming. The data-generator allows you to quickly create data, based on a schema, using a variety of generation modes for the purpose of testing and validation.

You can use this data generator to commission and maintain your test systems with data that conforms to your live environments without copying data or breaching GDPR.

The data generator has been written in Java to allow cross platform compatibility, allowing it to work on Microsoft Windows, Apple Mac and Linux equally.

## Getting started
As the data generator is at an _beta_ stage, there are no published releases. To be able to use this tool, please build the tool then you'll be able to use it as with the examples below.

### Usage examples

<!-- A few motivating and useful examples of how your project can be used. Spice this up with code blocks and potentially screenshots / videos ([LiceCap](https://www.cockos.com/licecap/) is great for this kind of thing) -->

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

## How it works

There are 3 discrete primary elements:

| Product | Description | Status | Notes |
| ---- | ---- | ---- | ---- |
| Profile | A representation of the data schema and rules which can be used to generate data | Stable | |
| Generator | A tool for generating data from a given profile | Stable - beta | Supports data generation and generation of data that has been [delibrately violated](./generator/docs/DeliberateViolation.md). |

See [overview](./docs/Profiles.md) and [schema](./docs/Schema.md) for details about profiles and how they work.

* [Generator](./generator/README.md)
  * [Generation algorithm](./generator/docs/GenerationAlgorithm.md)
  * [Set data generation process](./generator/docs/SetRestrictionAndGeneration.md)
  * [Decision trees](./docs/DecisionTrees/DecisionTrees.md)

* [Frequently asked questions](docs/FrequentlyAskedQuestions.md)

## Contributing

See [contributing](./.github/CONTRIBUTING.md) guidance and the [key decisions log](docs/KeyDecisions.md).

### Development setup

This product requires **Java version 1.8** to be installed. Later versions are not supported at present.
* Clone the repository
* Follow the steps in the [setup instructions](./generator/README.md). The following need to be actioned for the data generator product, the remaining steps need to be actioned to be able to use the profiler.
* Build the solution using your preferred method - the project is fully compatible with IntelliJ and Eclipse
* Run the data generator or profiler - see the examples below.

The product is confirmed compatible with Microsoft Windows, and should be compatible with all other operating systems, but this has not been confirmed to date.

## License

Copyright 2018 Scott Logic Ltd

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)

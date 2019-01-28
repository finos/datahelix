# Generator

A command-line tool for generating data according to [profiles](../docs/Profiles.md).

For a guide on how the generator may be used see the [step by step instructions](../docs/GettingStarted/StepByStepInstructions.md).

## Installation & Setup of IDE

See installation instructions [here](./docs/GeneratorSetup.md).

## Command line

Download the generator JAR file from the [GitHub project releases page](https://github.com/ScottLogic/data-engineering-generator/releases/).


Once [Java v1.8](https://www.java.com/en/download/manual.jsp) is installed you can run the generator with the following command:

`java -jar <path to JAR file> <command> [options] <arguments>`

* `<command>` one of the commands described below, `generate`, `visualise`, and so on
* `[options]` optionally a combination of options to configure how the command operates
* `<arguments>` required inputs for the command to operate

## Examples
* `java -jar generator.jar generate profile.json profile.csv`
* `java -jar generator.jar generate --violate=true profile.json violated-data-files/`

## Commands
### Generate
#### `generate [options] <profile path> <output path>`

Generates data to a specified endpoint.

* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the data should be emitted to. This will be a UTF-8 encoded CSV file or directory, option dependent.

The full list of generate options can be viewed [here](../docs/Options/GenerateOptions.md).

#

### Visualise
#### `visualise [options] <profile path> <output path>`

Generates a <a href=https://en.wikipedia.org/wiki/DOT_(graph_description_language)>DOT</a> compliant representation of the decision tree, 
for manual inspection, in the form of a gv file.
* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the tree DOT visualisation should be emitted to. This will be a UTF-8 encoded DOT file.

The full list of visualise options can be viewed [here](../docs/Options/VisualiseOptions.md) 

There may be other visualisers that are suitable to use. The requirements for a visualiser are known (currently) as:
- gv files are encoded with UTF-8, visualisers must support this encoding.
- gv files can include HTML encoded entities, visualisers should support this feature.

#
### Generate Tree JSON
#### `genTreeJson [options] <profile path> <output path>`

Generates a JSON file representing the in-memory decision tree.  This is a utility to assist the creation of `expected.json` files for `TreeTransformationIntegrationTest`

* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the tree JSON should be emitted to. This will be a UTF-8 encoded JSON file.

#### Options
Options are optional and case-insensitive

* `--partition`
   * Enables tree partitioning during transformation.
* `--optimise`
   * Enables tree optimisation during transformation. See [Decision tree optimiser](./docs/OptimisationProcess.md) for more details.

## Future invocation methods

* Calling into a Java library
* Contacting an HTTP web service

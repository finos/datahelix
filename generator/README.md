# Generator

A command-line tool for generating data according to [profiles](../docs/Profiles.md).

## Installation & Setup

See installation instructions [here](./docs/GeneratorSetup.md).

## Command line

You must have JAVA v1.8 installed (it can be [downloaded here](https://www.java.com/en/download/manual.jsp)) to be able to run the generator. Once this is installed you can run the generator by running the following command:

`java -jar <path to JAR file> <command> [options] <arguments>`

* `<command>` one of the commands described below, `generate`, `generateTestCases`, and so on
* `[options]` optionally a combination of options to configure how the command operates
* `<arguments>` required inputs for the command to operate

### For example
* `java -jar generator.jar generate profile.json profile.csv`
* `java -jar generator.jar generateTestCases profile.json violated-data-files/`

### __Command:__ `generate [options] <profile path> <output path>`

Generates data to a specified endpoint.

* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the data should be emitted to. This will be a UTF-8 encoded CSV file.

#### Options
Options are optional and case-insensitive

* `-n <rows>` or `--max-rows <rows>`
   * Emit at most `<rows>` rows to the output file, if not specified will limit to 10,000,000 rows
* `-t <generationType>` or `--t <generationType>`
   * Emit `<generationType>` data. Options are: `INTERESTING` (default) or `RANDOM`, `FULL_SEQUENTIAL`, see [Generation types](./docs/GenerationTypes.md) for more details
* `-c <combinationType>` or `--c <combinationTye>`
   * When producing data combine each data point using the `<combinationType>` strategy. Options are: `PINNING` (default), `EXHAUSTIVE`, `MINIMAL`, see [Combination strategies](./docs/CombinationStrategies.md) for more details.
* `-w <walker>` or `--w <walker>`
   * Use `<walker>` strategy for producing data. Options are: `CARTESIAN_PRODUCT` (default), `ROUTED`, `REDUCTIVE`, see [Tree walker types](./docs/TreeWalkerTypes.md) for more details.
* `--no-partition`
   * Prevent rules from being partitioned during generation. Partitioning allows for a (unproven) performance improvement when processing larger profiles.
* `--no-optimise`
   * Prevent profiles from being optimised during generation. Optimisation enables the generator to process profiles more efficiently, but adds more compute in other areas. See [Decision tree optimiser](./docs/OptimisationProcess.md) for more details.
* `-v`, `--v` or `--validate-profile`
   * Validate the profile, check to see if known contradictions exist, see [Profile validation](./docs/ProfileValidation.md) for more details
* `--trace-constraints`
   * When generating data emit a `<output path>.trace.json` file which will contain details of which rules and constraints caused the generator to emit each data point.

#### Examples

### __Command:__ `generateTestCases [options] <profile path> <output directory>`

Generates data to a specified directory, including both valid and [invalid data](./docs/DeliberateViolation.md).

* `<profile path>`, a path to the profile JSON file
* `<output directory>`, a directory path to where the output file/s should be emitted to. Files will be created for each rule, numbered relative to the position of the rule in the profile.

#### Options
Options are optional and case-insensitive

* `-n <rows>` or `--max-rows <rows>`
   * Emit at most `<rows>` rows to each output file, if not specified will limit to 10,000,000 rows
* `-t <generationType>` or `--t <generationType>`
   * Emit `<generationType>` data. Options are: `INTERESTING` (default) or `RANDOM`, `FULL_SEQUENTIAL`, see [Generation types](./docs/GenerationTypes.md) for more details
* `-c <combinationType>` or `--c <combinationTye>`
   * When producing data combine each data point using the `<combinationType>` strategy. Options are: `PINNING` (default), `EXHAUSTIVE`, `MINIMAL`, see [Combination strategies](./docs/CombinationStrategies.md) for more details.
* `-w <walker>` or `--w <walker>`
   * Use `<walker>` strategy for producing data. Options are: `CARTESIAN_PRODUCT` (default), `ROUTED`, `REDUCTIVE`, see [Tree walker types](./docs/TreeWalkerTypes.md) for more details.
* `--no-partition`
   * Prevent rules from being partitioned during generation. Partitioning allows for a (unproven) performance improvement when processing larger profiles.
* `--no-optimise`
   * Prevent profiles from being optimised during generation. Optimisation enables the generator to process profiles more efficiently, but adds more compute in other areas. See [Decision tree optimiser](./docs/OptimisationProcess.md) for more details.
* `-v`, `--v` or `--validate-profile`
   * Validate the profile, check to see if known contradictions exist, see [Profile validation](./docs/ProfileValidation.md) for more details
* `--trace-constraints`
   * When generating data emit a `<rule-output-file>.trace.json` file per rule output file which will contain details of which rules and constraints caused the generator to emit each data point.

### __Command:__ `genTreeJson [options] <profile path> <output path>`

Generates a JSON file representing the in-memory decision tree.  This is a utility to assist the creation of `expected.json` files for `TreeTransformationIntegrationTest`

* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the tree JSON should be emitted to. This will be a UTF-8 encoded JSON file.

#### Options
Options are optional and case-insensitive

* `--partition`
   * Enables tree partitioning during transformation.
* `--optimise`
   * Enables tree optimisation during transformation. See [Decision tree optimiser](./docs/OptimisationProcess.md) for more details.

### __Command:__ `visualise [options] <profile path> <output path>`

Generates a [DOT](https://en.wikipedia.org/wiki/DOT_(graph_description_language))-compliant representation of the decision tree, for manual inspection. You can also use this representation with a visualiser such as [Graphviz](https://www.graphviz.org/). 

* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the tree DOT visualisation should be emitted to. This will be a UTF-8 encoded DOT file.

#### Options
Options are optional and case-insensitive

* `-t <title>` or `--title <title>`
   * Include the given `<title>` in the visualisation. If not supplied, the description of in profile will be used, or the filename of the profile.
* `--no-title`
   * Exclude the title from the visualisation. This setting overrides `-t`/`--title`.
* `--no-optimise`
   * Prevents tree optimisation during visualisation.
* `--no-simplify`
   * Prevents tree simplification during visualisation. Simplification is where decisions with only one option are folded into the parent.

There may be other visualisers that are suitable to use. The requirements for a visualiser are known (currently) as:
- gv files are encoded with UTF-8, visualisers must support this encoding.
- gv files can include HTML encoded entities, visualisers should support this feature.


## Future invocation methods

* Calling into a Java library
* Contacting an HTTP web service

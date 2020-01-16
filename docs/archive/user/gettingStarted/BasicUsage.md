# Basic Usage

Once [Java v1.8](https://www.java.com/en/download/manual.jsp) is installed you can run the generator with the following command:

`java -jar <path to JAR file> [options] <arguments>`

* `[options]` optionally a combination of options to configure how the command operates
* `<arguments>` required inputs for the command to operate

**Note:** Do not include a trailing \ in directory paths

## Examples
* `java -jar generator.jar profile.json profile.csv`
* `java -jar generator.jar violate profile.json violated-data-files/`

Example profiles can be found in the [examples folder](../../../examples).

## Commands
### Generate
#### `[options] <profile path> <output path>`

Generates data to a specified endpoint.

* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the data should be emitted to. This will be a UTF-8 encoded CSV file or directory, option dependent.

The full list of generate options can be viewed [here](../commandLineOptions/GenerateOptions.md).

### Violate
#### `violate [options] <profile path> <output directory>`

Generates violating data to a specified folder/directory.

* `<profile path>`, a path to the profile JSON file.
* `<output directory>`, a path to a directory into which the data should be emitted.  This will consist of a set of output files, and a `manifest.json` file describing which constraints are violated by which output file.

The full list of violate options can be viewed [here](../commandLineOptions/ViolateOptions.md)

### Visualise
#### `visualise [options] <profile path> <output path>`

Generates a <a href=https://en.wikipedia.org/wiki/DOT_(graph_description_language)>DOT</a> compliant representation of the decision tree, 
for manual inspection, in the form of a gv file.
* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the tree DOT visualisation should be emitted to. This will be a UTF-8 encoded DOT file.

The full list of visualise options can be viewed [here](../commandLineOptions/VisualiseOptions.md) 

There may be other visualisers that are suitable to use. The requirements for a visualiser are known (currently) as:
- gv files are encoded with UTF-8, visualisers must support this encoding.
- gv files can include HTML encoded entities, visualisers should support this feature.


#### Options
Options are optional and case-insensitive

* `--partition`
   * Enables tree partitioning during transformation.
* `--optimise`
   * Enables tree optimisation during transformation. See [Decision tree optimiser](../../developer/algorithmsAndDataStructures/OptimisationProcess.md) for more details.

## Future invocation methods

* Calling into a Java library
* Contacting an HTTP web service

#
[< Previous](Visualise.md) | [Contents](StepByStepInstructions.md)

# Basic Usage

Once [Java v1.8](https://www.java.com/en/download/manual.jsp) is installed you can run the generator with the following command:

`java -jar <path to JAR file> [options] <arguments>`

* `[options]` optionally a combination of options to configure how the command operates
* `<arguments>` required inputs for the command to operate

**Note:** Do not include a trailing \ in directory paths

## Examples
* `java -jar generator.jar profile.json profile.csv`

Example profiles can be found in the [examples folder](../../../examples).

### Generation
#### `[options] <profile path> <output path>`

Generates data to a specified endpoint.

* `<profile path>`, a path to the profile JSON file
* `<output path>`, a file path to where the data should be emitted to. This will be a UTF-8 encoded CSV file or directory, option dependent.

The full list of generate options can be viewed [here](../commandLineOptions/GenerateOptions.md).

#### Options
Options are optional and case-insensitive

* `--optimise`
   * Enables tree optimisation during transformation. See [Decision tree optimiser](../../developer/algorithmsAndDataStructures/OptimisationProcess.md) for more details.

### Visualisation options

* `--visualiser-level <level>`
   * Configures the generator to generate decision tree visualisations to different LEVELs
   * Can be one of the following:
      * `OFF` (default) - Do not create visualisations
      * `STANDARD` - Create visualisations each time a decision tree is created or reduced*
      * `DETAILED` - Not currently used
* `--visualiser-output-folder <directory-path>`
   * Configures where the visualisations should be saved, must be a directory
   * This option is required when a `--visualiser-level` option is not `OFF`
   * Any existing files will be overwritten

\* Where a decision tree is processed and reduced in size, as part of problem solving. I.e. 'solve' one field, remove it from the tree and move on to the next - each time reducing the tree, by removing the 'solved' field.

Generates a <a href=https://en.wikipedia.org/wiki/DOT_(graph_description_language)>DOT</a> compliant representation of the decision tree, for manual inspection, in the form of a `.dot` file.

for example:

```
graph tree {
  bgcolor="transparent"
  label="ExampleProfile1"
  labelloc="t"
  fontsize="20"
  c0[bgcolor="white"][fontsize="12"][label="Column 1 Header is STRING
Column 2 Header is STRING"][shape=box]
c1[fontcolor="red"][label="Counts:
Decisions: 0
Atomic constraints: 2
Constraints: 1
Expected RowSpecs: 1"][fontsize="10"][shape=box][style="dotted"]
}
```

You may read a dot file with any text editor, or use a visualiser such as such as [Graphviz](https://www.graphviz.org/).

There may be other visualisers that are suitable to use. The requirements for a visualiser are known (currently) as:
* files are encoded with UTF-8, visualisers must support this encoding.
* files can include HTML encoded entities, visualisers should support this feature.

## Future invocation methods

* Calling into a Java library
* Contacting an HTTP web service

#
[< Previous](Visualise.md) | [Contents](StepByStepInstructions.md)

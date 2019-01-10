# Generator

A command-line tool for generating data according to [profiles](../docs/Profiles.md).

## Command line

The command line has two commands. Usage instructions for a command can be requested by calling it without arguments - eg, `dg generate`.

### `generate`

Generates data to a specified endpoint.

### `generateTestCases`

Generates data to a specified directory, including both valid and [invalid data](./docs/DeliberateViolation.md).

### `genTreeJson`

Generates a JSON file representing the in-memory decision tree.  This is a utility to assist the creation of `expected.json` files for `TreeTransformationIntegrationTest`

### `visualise`

Generates a [DOT](https://en.wikipedia.org/wiki/DOT_(graph_description_language))-compliant representation of the decision tree, for manual inspection. You can also use this representation with a visualiser such as [Graphviz](https://www.graphviz.org/). 

There may be other visualisers that are suitable to use. The requirements for a visualiser are known (currently) as:
- gv files are encoded with UTF-8, visualisers must support this encoding.
- gv files can include HTML encoded entities, visualisers should support this feature.

## Future invocation methods

* Calling into a Java library
* Contacting an HTTP web service

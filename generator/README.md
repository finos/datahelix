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

Generates a [DOT](https://en.wikipedia.org/wiki/DOT_(graph_description_language))-compliant representation of the decision tree, for manual inspection.

## Future invocation methods

* Calling into a Java library
* Contacting an HTTP web service

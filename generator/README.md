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

To visualise the tree, you can also use [Graphviz](https://www.graphviz.org/). 

Install the tool and create this file on your local environment editting the path to your installation of the tool:
File name: gviz.bat
File contents:
```
@echo off
set gviz="<your path>\Graphviz2.38\bin\dot.exe"
set file=%~1
set output=%tmp%\%~nx1.svg
%gviz% -Tsvg "%file%" > "%output%"
echo File saved as %output%.
start %output%
```

Next, drag and drop the .gv file created by the visualise command of the generator onto the bat file. The visualisation of the graph will open in your web browser.

## Future invocation methods

* Calling into a Java library
* Contacting an HTTP web service

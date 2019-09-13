# Visualising the Decision Tree
_This is an alpha feature. Please do not rely on it. If you find issues with it, please [report them](https://github.com/finos/datahelix/issues)._ 

This page will detail how to use the `visualise` command to view the decision tree for a profile.

Visualise generates a <a href=https://en.wikipedia.org/wiki/DOT_(graph_description_language)>DOT</a> compliant representation of the decision tree, 
for manual inspection, in the form of a gv file.

## Using the Command Line


To visualise the decision tree run the following command from the command line:

`java -jar <path to JAR file> visualise [options] --profile-file="<path to profile>" --output-path="<path to desired output GV file>"`

* `[path to JAR file]` the location of generator.jar
* `[options]` optionally a combination of [options](../commandLineOptions/VisualiseOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to desired output GV file>` the location of the folder for the resultant GV file of the tree

## Example

Using the [Sample Profile](ExampleProfile1.json) that was created in the [first](CreatingAProfile.md) section, run the visualise command
with your preferred above method. 

With no options this should yield the following gv file:

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

This is a very simple tree, more complex profiles will generate more complex trees

## Hints and Tips

* You may read a gv file with any text editor
* You can also use this representation with a visualiser such as [Graphviz](https://www.graphviz.org/).

    There may be other visualisers that are suitable to use. The requirements for a visualiser are known (currently) as:
    - gv files are encoded with UTF-8, visualisers must support this encoding.
    - gv files can include HTML encoded entities, visualisers should support this feature.

#
[< Previous](GeneratingData.md) | [Contents](StepByStepInstructions.md) | [Next Section >](BasicUsage.md)


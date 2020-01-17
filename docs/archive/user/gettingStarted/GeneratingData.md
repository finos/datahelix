# Generating Data

This page details how to generate data with a given profile.


## Using the Command Line

For first time setup, see the [Generator setup instructions](BuildAndRun.md).

To generate data run the following command from the command line

`java -jar <path to JAR file> generate [options] --profile-file="<path to profile>" --output-path="<desired output path>"`

* `[path to JAR file]` the location of generator.jar
* `[options]` optionally a combination of [options](../commandLineOptions/GenerateOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<desired output path>` the location of the generated data.  If this option is omitted, generated data will be streamed to the standard output.

## Example - Generating Data

Using the [Sample Profile](ExampleProfile1.json) that was created in the [previous](CreatingAProfile.md) section, run the following command:

 `java -jar <path to JAR file> generate --profile-file="<path to ExampleProfile1.json>" --output-path="<path to desired output file>"`

* `<path to desired output file>` the file path to the desired output file 

With no other options this should yield the following data:

|Column 1       |Column 2     |
|:-------------:|:-----------:|
|"Lorem Ipsum"	|-2147483648  |
|"Lorem Ipsum"	|0            |
|"Lorem Ipsum"	|2147483646   |
|"Lorem Ipsum"	|             |
|	            |-2147483648  |

## Hints and Tips

* The generator will output velocity and row data to the console as standard
(see [options](../commandLineOptions/GenerateOptions.md) for other monitoring choices).
    * If multiple monitoring options are selected the most detailed monitor will be implemented.
* Ensure any desired output files are not being used by any other programs or the generator will not be able to run.
    * If a file already exists it will be overwritten.
* Unless explicitly excluded `null` will always be generated for each field.

#
[< Previous](CreatingAProfile.md) | [Contents](StepByStepInstructions.md)

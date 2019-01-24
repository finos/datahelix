# Generating Data

This page details how to generate data with a given profile.

## Using the Command Line

To generate data run the following command from the command line

`java -jar <path to JAR file> generate [options] "<path to profile>" "<path to desired output directory>"`

* `[path to JAR file]` the location of generator.jar
* `[options]` optionally a combination of [options](../Options/GenerateOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to desired output directory>` the location of the resultant CSV file of generated data

## Example

Using the [Sample Profile](./ExampleProfile1.json) that was created in the [previous](./CreatingAProfile.md) section, run the generate command
with your preferred above method. 

With no options this should yield the following data:

|Column 1 Header |Column 2 Header|
|:--------------:|:-------------:|
|"Lorem Ipsum"   |"Lorem Ipsum"  |
|"Lorem Ipsum"   |
|                |"Lorem Ipsum"  |

## Hints and Tips

* Ensure the desired output file is not being used by any other programs or the generator will not be able to run
    * If the file already exists it will be overwritten

#
[< Previous](CreatingAProfile.md) | [Contents](StepByStepInstructions.md) | [Next Section >](GenerateTestCases.md)

# Generating Violating Data (Test Cases)
GenerateTestCases produces one file per rule violated along with a manifest.json file which lists the violated rules.

## Using the Command Line

To generate violating data run the following command from the command line:

`java -jar <path to JAR file> generateTestCases [options] "<path to profile>" "<path to desired output directory>"`

* `[path to JAR file]` the location of generator.jar
* `[options]` optionally a combination of [options](../Options/GenerateTestCasesOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to output desired directory>` the location of the folder in which to save the resultant CSV files of generated data


## Using an IDE 

The generator can be run using the following program arguments within your IDE:

`generateTestCases [options] "<path to profile>" "<path to desired output directory>"`

* `[options]` optionally a combination of [options](../Options/GenerateTestCasesOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to output desired directory>` the location of the folder in which to save the resultant CSV files of generated data

## Example

Using the [Sample Profile](./ExampleProfile1.json) that was created in the [first](./CreatingAProfile.md) section, run the generateTestCases command
with your preferred above method. 

With no options this should yield the following data:

|Column 1 Header |Column 2 Header|
|:--------------:|:-------------:|
|Lorem Ipsum	 |-2147483648    |
|                |-2147483648    |
|Lorem Ipsum	 |0              |
|Lorem Ipsum	 |2147483646     |
|Lorem Ipsum	 |1900-01-01T00:00|
|Lorem Ipsum	 |2100-01-01T00:00|
|Lorem Ipsum	 |               |
|-2147483648	 |Lorem Ipsum    |
|0	             |Lorem Ipsum    |
|2147483646      |Lorem Ipsum    |
|1900-01-01T00:00|Lorem Ipsum    |
|2100-01-01T00:00|Lorem Ipsum    |
|	             |Lorem Ipsum    |
|-2147483648	 |               |

The data generated violates each constraint in turn. As there is only one rule in the example profile this is all produced in one file.
By violating the `"ofType": "String"` constraint the violating data produced is of any type except string.

## Hints and Tips

* The output from generateTestCases will produce one output file per rule being violated
    * This is why the output location is a directory and not a file
    * If there are already files in the output directory they may be overwritten 
* It is important to give your rules descriptions so that the manifest can list the violated rules clearly
* Rules made up of multiple constraints will be violated as one rule and therefore will produce one output file per rule

#
[< Previous](GeneratingData.md) | [Contents](StepByStepInstructions.md) | [Next Section >](Visualise.md)


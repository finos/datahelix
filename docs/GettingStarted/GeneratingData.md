# Generating Data

This page details how to generate data with a given profile.

## Using the Command Line

To generate data run the following command from the command line

`java -jar <path to JAR file> generate [options] "<path to profile>" "<desired output path>"`

* `[path to JAR file]` the location of generator.jar
* `[options]` optionally a combination of [options](../Options/GenerateOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<desired output path>` the location of the generated data

## Example - Generating Valid Data

Using the [Sample Profile](./ExampleProfile1.json) that was created in the [previous](./CreatingAProfile.md) section, run the following command:

 `java -jar <path to JAR file> generate "<path to ExampleProfile1.json>" "<path to desired output file>"`

* `<path to desired output file>` the file path to the desired output file 

With no options this should yield the following data:

|Column 1 Header |Column 2 Header|
|:--------------:|:-------------:|
|"Lorem Ipsum"   |"Lorem Ipsum"  |
|"Lorem Ipsum"   |
|                |"Lorem Ipsum"  |

## Example - Generating Violating Data

The generator can be used to generate data which intentionally violates the profile constraints for testing purposes.

Using the `--violate` switch produces one file per rule violated along with a manifest that lists which rules are violated in each file.

Using the [Sample Profile](./ExampleProfile1.json) that was created in the [first](./CreatingAProfile.md) section, run the following command: 

`java -jar <path to JAR file> generate --violate=true "<path to ExampleProfile1.json>" "<path to desired output directory>"`

* `<path to desired output directory>` the location of the folder in which the generated files will be saved

With no additional options this should yield the following data:

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

The following manifest file is also produced: 

```
{
  "cases" : [ {
    "filePath" : "1",
    "violatedRules" : [ "Rule Description" ]
  } ]
}
```

## Hints and Tips

* Ensure any desired output files are not being used by any other programs or the generator will not be able to run
    * If a file already exists it will be overwritten
* Violated data generation will produce one output file per rule being violated
    * This is why the output location is a directory and not a file
    * If there are already files in the output directory they may be overwritten 
* It is important to give your rules descriptions so that the manifest can list the violated rules clearly
* Rules made up of multiple constraints will be violated as one rule and therefore will produce one output file per rule

#
[< Previous](CreatingAProfile.md) | [Contents](StepByStepInstructions.md) | [Next Section >](Visualise.md)

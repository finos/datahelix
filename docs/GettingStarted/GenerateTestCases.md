#Generating Violating Data (Test Cases)

You must have Java v1.8 installed (it can be [downloaded here](https://www.java.com/en/download/manual.jsp)) to be able 
to run the generator. Once this is installed you can run the generator in the following ways:

**GenerateTestCases produces one file per rule violated along with a manifest.json file which lists the violated rules**

##Using the Command Line

Download the Jar file (generator.jar) from the [GitHub project releases page](https://github.com/ScottLogic/data-engineering-generator/releases/).

To generate data run the following command from the command line

`java -jar <path to JAR file> generateTestCases [options] "<path to profile>" "<path to desired output directory>"`

* `[path to JAR file]` the location of generator.jar
* `[options]` optionally a combination of [options](../Options/GenerateOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to output directory>` the desired location of the folder for the resultant CSV files of generated data


##Using an IDE 

If you have not already, follow the instructions [here](../../generator/docs/GeneratorSetup.md) to set up the development environment

Once your IDE is set up the generator can be run using the following program arguments:

`generateTestCases [options] "<path to profile>" "<path to desired output CSV>"`

* `[options]` optionally a combination of [options](../Options/GenerateOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to output directory>` the desired location of the folder for the resultant CSV files of generated data



## Example


Using the [Sample Profile](./ExampleProfile1.json) that was created in the [previous](./CreatingAProfile.md) section, run the generate command
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

##Hints and Tips

* The output from generateTestCases will produce one output file per rule being violated
    * This is why the output location is a directory and not a file
    * If there are already files in the output directory they may be overwritten 
* It is important to give your rules descriptions so that the manifest can list the violated rules clearly
* Rules made up of multiple constraints will be violated as one rule and therefore will produce one output file



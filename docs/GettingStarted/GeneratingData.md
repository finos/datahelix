#Generating Data

You must have Java v1.8 installed (it can be [downloaded here](https://www.java.com/en/download/manual.jsp)) to be able 
to run the generator. Once this is installed you can run the generator in the following ways:

##Using the Command Line

Download the Jar file (generator.jar) from the [GitHub project releases page](https://github.com/ScottLogic/data-engineering-generator/releases/).

To generate data run the following command from the command line

`java -jar <path to JAR file> generate [options] "<path to profile>" "<path to desired output csv>"`

* `[path to JAR file]` the location of generator.jar
* `[options]` optionally a combination of [options](../Options/GenerateOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to output directory>` the desired location of the resultant CSV file of generated data


##Using an IDE 

If you have not already, follow the instructions [here](../../generator/docs/GeneratorSetup.md) to set up the development environment

Once your IDE is set up the generator can be run using the following program arguments:

`generate [options] "<path to profile>" "<path to desired output CSV>"`

* `[options]` optionally a combination of [options](../Options/GenerateOptions.md) to configure how the command operates
* `<path to profile>` the location of the JSON profile file
* `<path to output directory>` the desired location of the resultant CSV file of generated data



## Example

Using the [Sample Profile](./ExampleProfile1.json) that was created in the [previous](./CreatingAProfile.md) section, run the generate command
with your preferred above method. 

With no options this should yield the following data:

|Column 1 Header |Column 2 Header|
|:--------------:|:-------------:|
|"Lorem Ipsum"   |"Lorem Ipsum"  |
|"Lorem Ipsum"   |
|                |"Lorem Ipsum"  |


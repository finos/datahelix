### Example - Generating Violating Data

The generator can be used to generate data which intentionally violates the profile constraints for testing purposes.

Using the `violate` command produces one file per rule violated along with a manifest that lists which rules are violated in each file.

Using the [Sample Profile](#Example-Profile) that was created in a previous section, run the following command: 

`java -jar <path to JAR file> violate --profile-file="<path to ExampleProfile1.json>" --output-path="<path to desired output directory>"`

* `<path to desired output directory>` the location of the folder in which the generated files will be saved

Additional options are [documented here](https://github.com/finos/datahelix/blob/master/docs/user/commandLineOptions/ViolateOptions.md).

With no additional options this should yield the following data:

* `1.csv`:

|Column 1         |	Column 2       |
|:---------------:|:--------------:|
|-2147483648	  |-2147483648     |
|-2147483648	  |0               |
|-2147483648	  |2147483646      |
|-2147483648	  |                |
|0                |-2147483648     |
|2147483646	      |-2147483648     |
|1900-01-01T00:00 |-2147483648     |
|2100-01-01T00:00 |-2147483648     |
|	              |-2147483648     |

* `2.csv`:

|Column 1 Name	  |Column 2 Name   |
|:---------------:|:--------------:|
|"Lorem Ipsum"	  |"Lorem Ipsum"   |
|"Lorem Ipsum"	  |1900-01-01T00:00|
|"Lorem Ipsum"	  |2100-01-01T00:00|
|"Lorem Ipsum"	  |                |
|                 |"Lorem Ipsum"   |

* `manifest.json`:

```
{
  "cases" : [ {
    "filePath" : "1",
    "violatedRules" : [ "Column 1 is a string" ]
  }, {
    "filePath" : "2",
    "violatedRules" : [ "Column 2 is a number" ]
  } ]
}
```

The data generated violates each rule in turn and records the results in separate files.
For example, by violating the `"ofType": "String"` constraint in the first rule the violating data produced is of types *decimal* and *datetime*.
The manifest shows which rules are violated in which file. 

### Hints and Tips

* The generator will output velocity and row data to the console as standard
(see [options](https://github.com/finos/datahelix/blob/master/docs/user/commandLineOptions/GenerateOptions.md) for other monitoring choices).
    * If multiple monitoring options are selected the most detailed monitor will be implemented.
* Ensure any desired output files are not being used by any other programs or the generator will not be able to run.
    * If a file already exists it will be overwritten.
* Violated data generation will produce one output file per rule being violated.
    * This is why the output location is a directory and not a file.
    * If there are already files in the output directory with the same names they will be overwritten.
* It is important to give your rules descriptions so that the manifest can list the violated rules clearly.
* Rules made up of multiple constraints will be violated as one rule and therefore will produce one output file per rule.
* Unless explicitly excluded `null` will always be generated for each field.

# Build and run the generator

The instructions below explain how to download the generator source code, build it and run it, using a Java IDE.  This is the recommended setup if you would like to contribute to the project yourself.  If you would like to use Docker to build the source code and run the generator, [please follow these alternate instructions](../../developer/DockerSetup.md).

## Get Code

Clone the repository to your local development folder.

```
git clone https://github.com/finos/datahelix.git
```

## Installation Requirements

* Java version 1.8
* Gradle
* Cucumber
* Preferred: One of IntelliJ/Eclipse IDE

### Java

[Download JDK 8 SE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html).

*(Please note, this has been tested with jdk1.8.0_172 but later versions of JDK 1.8 may still work)*

In Control Panel: edit your environment variables; set `JAVA_HOME=C:\Program Files\Java\jdk1.8.0_172`.  
Add Java binary utilities to your `PATH` (`C:\Program Files\Java\jdk1.8.0_172\bin`).

### Gradle

Download and install Gradle, following the [instructions on their project website](https://docs.gradle.org/current/userguide/installation.html).

### IntelliJ IDE

Get IntelliJ. [EAP](https://www.jetbrains.com/idea/nextversion/) gives you all features of Ultimate (improves framework support and polyglot).

### Eclipse

Alternatively, download and install [Eclipse](https://www.eclipse.org/downloads/). Please note we do not have detailed documentation for using the generator from Eclipse.

### Cucumber

Add **Gherkin** and **Cucumber for Java** plugins (file > settings > plugins if using IntelliJ IDE).

Currently the tests cannot be run from the TestRunner class.

To run a feature file you’ll have to modify the configuration by removing .steps from the end of the Glue field.

An explanation of the particular syntax used can be found [here](https://github.com/finos/datahelix/blob/master/docs/CucumberSyntax.md).

## First time setup
### Command Line

Build the tool with all its dependencies:

`gradle build`

Check the setup worked with this example command:

`java -jar orchestrator\build\libs\generator.jar generate --replace --profile-file=docs/user/gettingStarted/ExampleProfile1.json --output-path=out.csv`

To generate valid data run the following command from the command line:

`java -jar <path to JAR file> generate [options] --profile-file="<path to profile>" --output-path="<desired output path>"`

* `[path to JAR file]` - the location of `generator.jar`.
* `[options]` - optionally a combination of [options](../commandLineOptions/GenerateOptions.md) to configure how the command operates.
* `<path to profile>` - the location of the JSON profile file.
* `<desired output path>` - the location of the generated data.

### IntelliJ

On IntelliJ's splash screen, choose "Open".

Open the repository root directory, `datahelix`.

Right-click the backend Module, `generator`, choose "Open Module Settings".

In "Project": specify a Project SDK (Java 1.8), clicking "New..." if necessary.  
Set Project language level to 8.

Open the "Gradle" Tool Window (this is an extension that may need to be installed), and double-click Tasks > build > build.
Your IDE may do this automatically for you.

Navigate to the [`App.java` file](../../../orchestrator/src/main/java/com/scottlogic/datahelix/generator/orchestrator/App.java). Right click and debug.

Now edit the run configuration on the top toolbar created by the initial run. Name the run configuration 'Generate' and under 'Program Arguments' enter the following, replacing the paths with your desired files:

```
generate --profile-file="<path to an example JSON profile>" --output-path="<desired output file path>"
```

For example, run this command:
```
java -jar orchestrator\build\libs\generator.jar generate --replace --profile-file=docs/user/gettingStarted/ExampleProfile1.json --output-path=out.csv
```

Run this configuration to test that installation is successful.

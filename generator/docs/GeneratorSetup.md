# Generator Setup Instructions

## Installation Requirements

* Java version 1.8
* Cucumber
* One of IntelliJ/Eclipse IDE 

### Java

[Download JDK 8 SE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 

(*Please note, this has been tested with jdk1.8.0_172 but later versions of JDK 1.8 may still work)*

In Control Panel: edit your environment variables; set `JAVA_HOME=C:\Program Files\Java\jdk1.8.0_172`.  
Add Java binary utilities to your `PATH` (`C:\Program Files\Java\jdk1.8.0_172\bin`).

### IntelliJ IDE

Get IntelliJ. [EAP](https://www.jetbrains.com/idea/nextversion/) gives you all features of Ultimate (improves framework support and polyglot).

### Eclipse

Alternatively, download and install [Eclipse](https://www.eclipse.org/downloads/) but please note we do not have detailed documentation for using the generator from Eclipse.

### Cucumber

Add **Gherkin** and **Cucumber for Java** plugins (file > settings > plugins if using IntelliJ IDE) 

Currently the tests cannot be run from the TestRunner class

To run a feature file you’ll have to modify the configuration by removing .steps from the end of the Glue field 

An explanation of the particular syntax used can be found [here](https://github.com/ScottLogic/data-engineering-generator/blob/master/docs/CucumberSyntax.md) 

## First time setup

### Getting the code

Clone the repository to your local development folder.

```
git clone https://github.com/ScottLogic/data-engineering-generator.git 
```

### IntelliJ

On IntelliJ's splash screen, choose "Open".

Open the repository root directory, `data-engineering-generator`.

Right-click the backend Module, `generator`, choose "Open Module Settings".

In "Project": specify a Project SDK (Java 1.8), clicking "New..." if necessary.  
Set Project language level to 8.

Open the "Maven Projects" Tool Window, and double-click _Lifecycle > compile_.  
This is only necessary when your Maven dependencies change. Otherwise prefer the IDE's built-in Build.

Navigate to the `App.java` file (...\data-engineering-generator\generator\src\main\java\com\scottlogic\deg\generator\App.java). Right click and debug - *this will fail*.

Now edit the run configuration on the top toolbar created by the initial run. Name the run configuration 'Generate' and under 'Program Arguments' enter the following, replacing the paths with your desired locations:

```
generate "<path to an example JSON profile>" "<path to desired output CSV>"
```

Additionally create another run configuration called GenerateTestCases and add the program arguments

```
generateTestCases "<path to an example JSON profile>" "<path to desired output folder for generated CSVs>"
```

Run both of these configurations to test that installation is successful.

### Testing Maven Configuration

From IntelliJ, open the Maven window.

Under the main Data Engineering > Lifecycle tab run the "install" build. Once this completes run the Data Engineering Lifecycle > "compile" build. These should complete without error. Running the install in this way will make the jar artefacts available for use in other Maven builds.

Under the generator > Lifecycle tab run the compile build. This should complete without errors but we have had some developers experiencing "Missing jar artefact" errors. Using the Maven install build on the main Lifecycle should fix this.

Once you have confirmed that install and compile works try running the test build for the generator. This should run all of the tests in the generator folder as will happen on the automated AWS build. 

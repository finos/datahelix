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

Alternatively, download and install [Eclipse](https://www.eclipse.org/downloads/).

### Cucumber

Add **Gherkin** and **Cucumber for Java** plugins (file > settings > plugins if using Intellij IDE) 

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

### Eclipse

You can set up an existing Eclipse to support Scala or set up a new Eclipse with Scala support

#### Set up an existing Eclipse installation

Your Eclipse will need the `scala-ide` plug-in and the `m2e-scala` connector

##### `scala-ide`

You can follow the installation instruction on 
[here](http://scala-ide.org/docs/current-user-doc/gettingstarted/index.html)

As of this writing, we are using `Scala IDE for Eclipse` version 
4.7.1.v-2_12-20180102121323-2dfe808 and `Scala 2.12.3` version 4.7.1.201801011322 (Note: 
this version also supports Scala 2.11, the version that Spark needs).  Other versions may also work. 

##### `m2e-scala` connector

Adding the following url as a "Repository" in Eclipse (Help > Install New Software... > Add...)

`http://alchim31.free.fr/m2e-scala/update-site/`

And from there you install `Maven Integration for Scala IDE` (we use version 0.5.1.201410131304)

#### Set up a new Eclipse installation with Scala support

Get the [scala-ide](http://downloads.typesafe.com/scalaide-pack/4.7.0-vfinal-oxygen-212-20170929/scala-SDK-4.7.0-vfinal-2.12-win32.win32.x86_64.zip)
version of Eclipse.

Extract the archive, launch Eclipse and choose your workspace location.

#### Import the project as a Maven project.

For example, in Eclipse, File > Import... > Maven > Existing Maven Project, and then point to the 
top-level directory of this project `data-engineering-generator`

When importing is complete some errors relating to Scala versions may be shown.
To fix this:

- Right-click the *profiler* project in the Package Explorer view and click
    *Properties*
 - Select *Scala Compiler*, tick *Use Project Settings*, then select *Latest
    2.11 bundle*
 - Click *Apply and Close*

After the project has been rebuilt the errors should be gone.

#### Run

The appropriate run configuration is stored in the repository and so can be run
straight away:

- In the top toolbar, click the arrow next to the green start button
- Click *Profiler*

To change the arguments the Profiler is run with, edit the run configuration:

- In the top toolbar, click the arrow next to the green start button
- Click *Run Configurations* > *Scala Application* > *Profiler*
- Click the *Arguments* tab

Don't accidentally commit your changes to the run configuration.

As per the argument defined in the launch configuration, you should find an output file 
`test-output.json` in your current directory (which is 
`data-engineering-generator\profiler`)
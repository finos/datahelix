# Profiler

## Setup

The `profiler` module contains Scala code so you have to set up your IDE to support Scala. It also depends on Spark and Hadoop.

The following instructions are requirements to develop the profiler. If you just want to build the project as a whole (for instance, to work on the generator), you can skip setup of Spark, Hadoop, etc. 

### JDK

Install [Oracle JDK 8 SE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Scala does not support JDK 9+.

In Control Panel: edit your environment variables; set `JAVA_HOME=C:\Program Files\Java\jdk1.8.0_172`.  
Add Java binary utilities to your `PATH` (`C:\Program Files\Java\jdk1.8.0_172\bin`).

### Scala

Please follow the instruction [here](https://www.scala-lang.org/download/).  Note that if
you use an IDE (IntelliJ or Eclipse) then your Scala may come with the IDE package 
(e.g IntelliJ EAP, see below) or a plug-in for the IDE (e.g. `scala-ide` for Eclipse)

Spark does not support Scala 2.12.X yet. So you will need to set up Scala 2.11.12 in your environment.

### Spark

[Download Spark](https://spark.apache.org/downloads.html).

Choose Spark 2.3.1 -- same as our client bindings (see `org.apache.spark:spark-core_*:*` in `pom.xml`).

Choose "Pre-built for Apache Hadoop 2.7 and later".

Extract such that a folder `C:\spark-2.3.1-bin-hadoop2.7\bin` exists.

In Control Panel: edit your environment variables; set `SPARK_HOME=C:\spark-2.3.1-bin-hadoop2.7`.  
Add Spark binary utilities to your `PATH` (`C:\spark-2.3.1-bin-hadoop2.7\bin`).

### Hadoop

[Download Hadoop _binary_](https://hadoop.apache.org/releases.html)

Choose Hadoop 2.7.6 -- same as the version that your Spark distribution was built against.

Extract such that a folder `C:\hadoop-2.7.6\bin` exists.

In Control Panel: edit your environment variables; set `HADOOP_HOME=C:\hadoop-2.7.6`.  
Add Hadoop binary utilities to your `PATH` (`C:\hadoop-2.7.6\bin`).

### Windows shims

[winutils](https://github.com/steveloughran/winutils) is a set of POSIX compatibility shims for Hadoop.

Clone the repository:

```bash
git clone https://github.com/steveloughran/winutils
```

**Without replacing existing files**: copy the contents of `hadoop-2.7.1\bin` into `C:\hadoop-2.7.6\bin`.  
Such that `C:\hadoop-2.7.6\bin\winutils.exe` and friends exist, but such that you do not overwrite your existing 2.7.6 hadoop.dll.

### Cucumber 

Add Gherkin and Cucumber for Java plugins (file > settings > plugins if using Intellij IDE) 

Currently the tests cannot be run from the TestRunner class

To run a feature file you’ll have to modify the configuration by removing .steps from the end of the Glue field 

An explanation of the particular syntax used can be found [here](https://github.com/ScottLogic/data-engineering-generator/blob/master/docs/CucumberSyntax.md) 

### IntelliJ IDE

Get IntelliJ. [EAP](https://www.jetbrains.com/idea/nextversion/) gives you all features of Ultimate (improves framework support and polyglot).

On IntelliJ's splash screen, choose "Open".

Open the repository root directory, `data-engineering-generator`.

Right-click the backend Module, `generator`, choose "Open Module Settings".

In "Project": specify a Project SDK (Java 1.8), clicking "New..." if necessary.  
Set Project language level to 8.

Open the "Maven Projects" Tool Window, and double-click _Lifecycle > compile_.  
This is only necessary when your Maven dependencies change. Otherwise prefer the IDE's built-in Build.

Now click on `File -> Project structure` on the main menu

Select `Global libraries` from the left hand side of the settings window.

If you can't see `scala-sdk-2.11.12` in there, you will need to add it by clicking on the `+` button at the top of the window (`New global library`) and then selecting `Scala SDK`.

Now select `Modules` from the left hand side of the project structure window.

Select the `profiler` module.

Make sure `scala-sdk-2.11.12` has been added to the profiler's module list and other Scala SDK versions have been removed from it.

##### Plugins

_File > Settings_

Ensure you have the Scala plugin installed.

##### Recommended IntelliJ settings

_File > Settings_

Search "Show tool window bars". Ensure this is _checked_.

##### Run

In IntelliJ, navigate to `App.scala`.

There will be a Play button in the gutter of `def main()`.  
Click this and choose "Debug 'App'".

**This will fail** (no file specified in program arguments), but it gives us a Run Configuration.

Edit the Run Configuration...

#### Arguments

In Program Arguments, specify the path to an input csv for analysis, and an output path to a directory

For example, you may set your input path to `gfx_cleaned.csv` (found in test/resources), and
output path to the current directory (`.`):

```bash
"C:\git\data-engineering-generator\profiler\src\test\resources\gfx_cleaned.csv"
```

#### VM Options

Our runtime dynamically links to hadoop.dll (native code, via JNI).  
Specify the folder in which hadoop.dll lives:

```bash
-Djava.library.path="C:\hadoop-2.7.6\bin"
```

_Example_
```bash
"C:\git\data-engineering-generator\profiler\src\test\resources\gfx_cleaned.csv" .
```

#### Environment

You can specify `HADOOP_HOME` and `SPARK_HOME` here (or just rely on your Windows environment variables).

### Eclipse IDE

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

## Test input files and expected output files (temporary solution)

For now we keep track of some input files and their expected output file under these two folders

- `profiler\src\test\resources\`
- `profiler\src\test\resources\expected_output\`

This is only a temporary solution.  Please refer to the `README.md` file in `profiler\src\test\resources\expected_output\` for more details.

## Submitting a Spark job

Output a jar artifact to `target/data-engineering-generator-1.0-SNAPSHOT.jar`:

```bash
mvn package
```

Submit the jar file:

```bash
spark-submit target/data-engineering-generator-1.0-SNAPSHOT.jar
```

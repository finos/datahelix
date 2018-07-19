# Profile com.scottlogic.deg.generator.Generator

## Setup

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

#### Windows shims

[winutils](https://github.com/steveloughran/winutils) is a set of POSIX compatibility shims for Hadoop.

Clone the repository:

```bash
git clone https://github.com/steveloughran/winutils
```

**Without replacing existing files**: copy the contents of `hadoop-2.7.1\bin` into `C:\hadoop-2.7.6\bin`.  
Such that `C:\hadoop-2.7.6\bin\winutils.exe` and friends exist, but such that you do not overwrite your existing 2.7.6 hadoop.dll.

### JDK

Install [Oracle JDK 8 SE](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Scala does not support JDK 9+.

In Control Panel: edit your environment variables; set `JAVA_HOME=C:\Program Files\Java\jdk1.8.0_172`.  
Add Java binary utilities to your `PATH` (`C:\Program Files\Java\jdk1.8.0_172\bin`).

### IDE

Get IntelliJ. [EAP](https://www.jetbrains.com/idea/nextversion/) gives you all features of Ultimate (improves framework support and polyglot).

On IntelliJ's splash screen, choose "Open".

Open the repository root directory, `data-engineering-generator`.

Right-click the backend Module, `data-generator-spark`, choose "Open Module Settings".

In "Project": specify a Project SDK (Java 1.8), clicking "New..." if necessary.  
Set Project language level to 8.

Open the "Maven Projects" Tool Window, and double-click _Lifecycle > compile_.  
This is only necessary when your Maven dependencies change. Otherwise prefer the IDE's built-in Build.

#### Plugins

_File > Settings_

Ensure you have the Scala plugin installed.

#### Recommended IntelliJ settings

_File > Settings_

Search "Java Compiler". Change this to "Use compiler: Eclipse".  
This gives you faster builds.

Search "Show tool window bars". Ensure this is _checked_.

## Run

In IntelliJ, navigate to `App.scala`.

There will be a Play button in the gutter of `def main()`.  
Click this and choose "Debug 'App'".

**This will fail** (no file specified in program arguments), but it gives us a Run Configuration.

Edit the Run Configuration...

### Arguments

In Program Arguments, specify the path to a csv for analysis.

For now, I run it upon `gfx_cleaned.csv` (found in test/resources):

```bash
"C:\git\data-engineering-generator\data-generator-spark\src\test\resources\gfx_cleaned.csv"
```

### VM Options

Our runtime dynamically links to hadoop.dll (native code, via JNI).  
Specify the folder in which hadoop.dll lives:

```bash
-Djava.library.path="C:\hadoop-2.7.6\bin"
```

### Environment

You can specify `HADOOP_HOME` and `SPARK_HOME` here (or just rely on your Windows environment variables).

## Submitting a Spark job

Output a jar artefact to `target/data-engineering-generator-1.0-SNAPSHOT.jar`:

```bash
mvn package
```

Submit the jar file:

```bash
spark-submit target/data-engineering-generator-1.0-SNAPSHOT.jar
```

### Data Engineering Generator Project

##### Setup
```mvn clean install```

Minimal Maven step to create target:

```mvn package```

#
#### Run
It is simpler to use an IDE such as IntelliJ and create a run configuration for App.scala.
Using an IDE will enable debugging and toggling tests conveniently.

To run on the terminal:
```mvn scala:run -DmainClass=com.scottlogic.deg.App```


#### Submitting as a Spark job
First build a jar file with
```mvn clean install```

Then submit the jar file (requires spark installed)
```spark-submit data-engineering-generator.jar```

#### Writing Tests
Testing uses JUnit as the test runner. ScalaTest can be used to implement
various specifications such as AAA, BDD, acceptance, etc.

To run tests, they must be delcared in the pom.xml
```
<!-- Comma separated list of JUnit test class names to execute -->
<jUnitClasses>com.scottlogic.example.ClassNameTest</jUnitClasses>
```
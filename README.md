### Data Engineering Generator Project

##### Setup
```mvn clean install```

#
#### Run
```mvn scala:run -DmainClass=com.scottlogic.deg.App```


#### Submitting as a Spark job
First build a jar file with
```mvn clean install```

Then submit the jar file (requires spark installed)
```spark-submit data-engineering-generator.jar```

#### Writing Tests
To run tests, they must be delcared in the pom.xml
```
<!-- Comma separated list of JUnit test class names to execute -->
<jUnitClasses>com.scottlogic.example.ClassNameTest</jUnitClasses>
```
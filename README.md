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

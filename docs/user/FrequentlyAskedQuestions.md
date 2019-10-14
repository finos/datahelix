# Frequently asked questions

## What's the difference between formattedAs and granularTo?

The below constraints are superficially similar:

```javascript
{ "field": "price", "granularTo": 0.01 }
{ "field": "price", "is": "formattedAs", "value": "%.2f" }
```

The difference is that `granularTo` affects which numbers are generated, while `formattedAs` affects how they are converted to strings. In fact, if the data is output in some format that doesn't require numbers to be converted to strings (eg, CSV does but JSON does not) then `formattedAs` will have no effect whatsoever on a numeric value.

Consider this example:

```javascript
{ "field": "price", "granularTo": 0.01 },
{ "not": { "field": "price", "is": "null" } },
{ "field": "price", "greaterThanOrEqualTo": 0 },
{ "field": "price", "lessThanOrEqualTo": 1 },
```

This combination of constraints will generate values `[ 0, 0.01, 0.02, 0.03, ..., 1 ]`. If outputted to a CSV file, they would be printed exactly as in that list. If this presentational constraint were added:

```javascript
{ "field": "price", "is": "formattedAs", "value": "%.1f" }
```

then our CSV might look like this instead:
 
```csv
price
0.0
0.0
0.0
0.0
[...]
1.0
```

while the same data output to JSON would retain the original full precision:

```javascript
[
    { "price": 0 },
    { "price": 0.01 },
    { "price": 0.02 },
    { "price": 0.03 },
    ...
    { "price": 1 },
]
```

To reiterate, `formattedAs` only affects how data is presented _after_ it has been generated. It has no impact on _what_ data gets generated, and can be ignored entirely for many data types and output formats. 

## Does `inSet` allow `null` to be produced?
**Yes**

The `inSet` operator only defines the initial set of data to work from, but does not convey any instruction or definition that null is not permitted.

To explicitly prevent null, use `inSet` with `not null`.

The operator will not let the user explicitly put `null` into an `inSet`. So, using `inSet [null]` (or any set that contains `null`) will throw an error and abort processing.

For more details see the [set restriction and generation](SetRestrictionAndGeneration.md) page.

## Does `equalTo` allow `null` to be produced?
**No**

For more details see the [set restriction and generation](SetRestrictionAndGeneration.md) page.

## Why are we continuing to use an out-of-date JDK

Currently we're using version 1.8 of the Java development toolkit (JDK), there are newer versions which fix some issues found in 1.8.

Our product roadmap shows that we plan to develop a tool for analysing data sources and producing an indicative profile from them, a tool we call the profiler. It is currently planned for this tool to integrate with Apache Spark using Scala. This integration requires a compatible version of the JDK, version 1.8 is the latest compatible version.

We could upgrade the version of the JDK for the generator independently of this tool, however this would require an additional installation of Java on a users environment. We are trying to create a tool with the least friction when it comes to its use, as such we have decided - for now at least - to stick with version 1.8.

## What problems have you had to solve when using an older JDK

The following problems have been identified and resolved:

### a) `flatMap` is a blocking call 
The JDK version of `flatMap` calls onto `forEach` under the hood, which in turn evaluates the whole input `Stream` before returning any values.

We are using the Java `Streams` api as a means to lazily create and consume data, as such we need to ensure that `flatMap` can accept a stream of values and emit them as soon as possible. Another example is where `RANDOM` data generation is employed. `RANDOM` will generate an infinite stream of data from the data sources (row limiting happens as a call to `limit` later, where the streams are consumed before emitting to the output). As such using the JDK edition of `flatMap` would never complete, as the `RANDOM` stream never finishes emitting data.

We workaround this problem by using a _shim_ found on [Stackoverflow](https://stackoverflow.com/) (credits below), which is used widely across the generator. Whenever using a `flatMap` call it is advised that the shim be used instead of the JDK version. Only where calculated and express reasons should the JDK edition be used. This approach will reduce the chances that a blocking `flatMap` call will used by accident, especially in an area where we want to ensure data is streamed out.

Therefore instead of:

```
return streamOfStreams.flatMap(
    stream -> doSomethingToFlatten(stream));
```

We would use the following workaround:

```
return FlatMappingSpliterator.flatMap(
    streamOfStreams,
    stream -> doSomethingToFlatten(stream));
```

#### Credits
Author: [Holger](https://stackoverflow.com/users/2711488/holger)  
Question: [In Java, how do I efficiently and elegantly stream a tree node's descendants?](https://stackoverflow.com/questions/32749148/in-java-how-do-i-efficiently-and-elegantly-stream-a-tree-nodes-descendants/32767282#32767282)
# Deliberate violation

_This is an alpha feature. Please do not rely on it. If you find issues with it, please [report them](https://github.com/finos/datahelix/issues)._ 

## Invocation

Violation is invoked using the `violate` command. An output directory must be specified rather than a single file.

## Algorithm

1. For each rule `R`:
	1. Create a new version of the profile where `R` is wrapped in a `ViolationConstraint`. A violation constraint works similar to a not constraint except that `and` conditionals are treated differently (see below).
	1. Create a decision tree from that profile, and pass it to the generator as normal.
	1. Write the output to a file in the output directory with a numerical file name.
1. Output a valid version with no rules violated.
1. Output a manifest file, listing which output file corresponds to which rule.

## Manifest

An example of a manifest file when violating one rule. "001.csv" is where the first rule has been violated.
```javascript
[
  {
    "filepath": "001.csv",
    "violatedRules": [ "Price field should not accept nulls" ]
  }
]
```

## ViolationConstraint

The `violation` constraint is an internal constraint and cannot be used specifically, only for violation. It works exactly like a `not` constraint, except where dealing with `and` constraints.

* A `not` constraint converts `¬AND(X, Y, Z)` into `OR(¬X, ¬Y, ¬Z)`
* A `violate` constraint converts `VIOLATE(AND(X, Y, Z))` into:
```
OR(
 AND(VIOLATE(X), Y, Z),
 AND(X, VIOLATE(Y), Z),
 AND(X, Y, VIOLATE(Z)))
```

This is so that we end up with each inner constraint violated separately.

## Generating invalid data

One of the most powerful features of the generator is its ability to generate data that violates constraints.To create invalid data use the `violate` command. This time you need to specify an output directory rather than a file:

```
$ java -jar generator.jar violate --max-rows=100 --replace --profile-file=profile.json --output-path=out
```

When the above command has finished, you'll find that the generator has created an `out` directory which has four files:

```
$ ls out
1.csv           2.csv           3.csv           manifest.json
```

The manifest file details which rules have been violated in each of the output files:

```
{
  "cases": [
    {
      "filePath": "1",
      "violatedRules": ["first name"]
    },
    {
      "filePath": "2",
      "violatedRules": ["age"]
    },
    {
      "filePath": "3",
      "violatedRules": ["national insurance"]
    }
  ]
}
```

If you take a look at the generated file `1.csv` you'll see that data for the `firstName` field does not obey the given constraints, whilst those for `age` and `nationalInsurance` are correct:

```
firstName,age,nationalInsurance
-619248029,71,"HT849919"
08-12-2001 02:53:16,15,
"Lorem Ipsum",11,
"Lorem Ipsum",71,"WX004081"
1263483797,19,"HG054666"
,75,"GE023082"
"Lorem Ipsum",59,"ZM850737C"
[...]
```

However, it might be a surprise to see nulls, numbers and dates as values for the `firstName` field alongside strings that do not match the regex given in the profile. This is because these are all defined as a single rule within the profile. You have a couple of options if you want to ensure that `firstName` is null or a string, the first is to inform the generator that it should not violate specific constraint types:

```
$ java -jar generator.jar violate --dont-violate=ofType \
  --max-rows=100 --replace --profile-file=profile.json --output-path=out
```

Or, alternatively, you can re-arrange your constraints so that the ones that define types / null, are grouped as a single rule. After By re-grouping constraints, the following output, with random strings that violate the regex constraint, is generated:

```
firstName,age,nationalInsurance
"�",43,"PT530853D"
"뷇",56,"GE797875M"
"邦爃",84,"JA172890M"
"J㠃懇圑㊡俫杈",32,"AE613401F"
"俵튡",38,"TS256211F"
"M",60,"GE987171M"
"M",7,
"Mꞎኅ剭Ꙥ哌톞곒",97,"EN082475C"
")",80,"BX025130C"
",⑁쪝",60,"RW177969"
"5ᢃ풞ﺯ䒿囻",57,"RY904705"
[...]
```
**NOTE** we are considering adding a feature that allows you to [specify / restrict the character set](https://github.com/finos/datahelix/issues/294) in a future release.

## Known issues
1. The process would be expected to return vast quantities of data, as the single constraint `foo inSet [a, b, c]` when violated returns all data except [a, b, c] from the universal set. Whilst logically correct, could result in a unusable tool/data-set due to its time to create, or eventual size.
1. The process of violating constraints also violates the type for fields, e.g. `foo ofType string` will be negated to `not(foo ofType string)`. This itself could be useful for the user to test, but could also render the data unusable (e.g. if the consumer requires the 'schema' to be adhered to)
1. The process of violating constraints also violates the nullability for fields, e.g. `foo not(is null)` will be negated to `foo is null`. This itself could be useful for the user to test, but could render the data unusable (e.g. if the consumer requires non-null values for field `foo`).
1. Implied/default rules are not negated, therefore as every field is implied/defaulted to allowing nulls, the method of violation currently doesn't prevent null from being emitted when violating. This means that nulls can appear in both normal data generation mode AND violating data generation mode.

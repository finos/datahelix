# Output tracing

This facility will permit analysis of which rules* and constraints from the profile have been employed to emit the value for each field in each row. Each _atomic constraint_ will be emitted. Gramatical constraints (i.e. _AND_, _OR_, _IF_, _allOf_ and _anyOf_) are not included, however the negation-constraint (_NOT_) will be.

It is normal for there to be multiple constraints to be emitted per field value, none of these should be contradictory, see the example below for a view of what the file could look like.

Enabling this facility when generating data will:
* Generate the data as per normal
* Generate (progressively)*** a JSON formatted file named _&lt;output-file-name&gt;_-trace.json with the meta information (see structure below)

## Notes on output
__\*__ Rules are not currently emitted  
__\*\*__ Note: this facility is not currently configurable, it requires a rebuild with the `SourceTracingDataSetWriter` used with the `MultiDataSetWriter` in  `Generate.java`.   
__\*\*\*__ This JSON file is generated progressively, as such you can read the file with a text-editor during generation but it will not be valid JSON until the generator has exited. In the above example the final `]` will only be emitted after rows have been emitted.

## JSON structure
The JSON file will have the following structure:
* An array of _row information_ objects, one object for each row in the data output file
* Each _row information_ object will contain a collection of _field value information_ objects, one object per field in the profile

### Field Value Information
| Property | Type | Meaning | Example |
| ---- | ---- | ---- | ---- |
| constraints | array of _constraint information_ | Details of the constraints employed to restrict the value | [ { _constraint information_ }] |
| rule* | string | name of the rule* involved in the restriction of the value | "rule 1" |
| field | string | name of the field | "field 1" |

### Constraint Information
| Property | Type | Meaning | Example | 
| ---- | ---- | ---- | ---- |
| type | string | the type name of the constraint | "IsInSetConstraint" |
| value | string | a string representation of the constraint | "\`field 1\` in [ value1, value2 ]" |

### Example

_example of a trace output file for a output file with 1 row and 2 fields_
```
[
 [ {
  "constraints" : [ {
    "type" : "IsInSetConstraint",
    "value" : "`field 1` in [value 1, value 2]"
  }, {
    "type" : "NotConstraint",
    "value" : "NOT(`MUST_BE_NULL`: field 1)"
  } ],
  "rule" : null,
  "field" : "field 1"
 }, {
   "constraints" : [ {
     "type" : "IsInSetConstraint",
     "value" : "`field 2` in [A, B]"
   }, {
     "type" : "IsNullConstraint",
     "value" : "`MUST_BE_NULL`: field 2"
   } ],
   "rule" : null,
   "field" : "field 2"
 } ]
]
```

#### Explanation
* Array of _row information_ instances, same number as number of rows in output file
  * Array of _field value information_ instances, each instance contains information about the row in the output file at the same position. Each _field value information_ should be in the same order as in the output row, but this is not guarenteed, check the `field` property.
    * An array of _constraint information_ that influenced the generation of the value in this field in the given row
    * The name of the rule* that contained the constraint/s
    * The name of the field that this _field value information_ relates to

## Future changes
In future this file will also contain information on violated rules, etc. The structure/shape of the file may change to support reporting sufficient information in these cases.
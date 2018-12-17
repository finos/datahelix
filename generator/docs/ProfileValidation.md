# Profile Validation

The profile validator analyzes a profile to check if it is valid. A valid profile is one that can generate data. When an invalid profile is observed, the process will terminate and notify the user of the validation errors in the profile. 

The validation checks currently performed include:

1. All data values are excluded from the possible range

    Example: A is in set [3] and A is not in set [3] - there are no values that A can be.

2. Constraint for a different data type is applied

    Example: A is a string and granularity constraint is applied - granularity constraint can only operate on numeric fields at the moment.

The workflow for the profile validator is that it iterates over all the constraints in the profile and for each field it keeps a running collection of restrictions that must be observed in order for the profile to remain valid. When an invalidating constraint is encountered, a validation alert is raised and the restrictions for this field remain unchanged. All validation alerts for all fields will be reported at the end of the process.

Validation alerts have different levels:
| Criticality |      Data generation      |                                                                                                     Explaination |
|-------------|:-------------------------:|-----------------------------------------------------------------------------------------------------------------:|
| Error       | Data cannot be generated. |                       There are unrecoverable contradictions between constraints. These errors must be resolved. |
| Warning     |   Data can be generated.  | There are constraints that look like they may be incorrect but there are some possible values that satisfy them. |
| Information |   Data can be generated.  |                                                            There are implicit assumptions made about the fields. |

There are a number of checks performed depending on the constraint applied. Below we explain them.

## Type restrictions

The profile supports 3 valid types for fields: NUMERIC, STRING and TEMPORAL.

There are a number of ways in which a type may be enforced for a field, including:

| Contstraint                               | Type restriction |
|-------------------------------------------|------------------|
| A is of type STRING                       | STRING           |
| A is after 2008-09-15T15:53:00.000        | TEMPORAL         |
| A is before 2008-09-15T15:53:00.000       | TEMPORAL         |
| A is after or at 2008-09-15T15:53:00.000  | TEMPORAL         |
| A is before or at 2008-09-15T15:53:00.000 | TEMPORAL         |
| A is shorter than 10                      | STRING           |
| A is longer than 1                        | STRING           |
| A is granular to 1                        | NUMERIC          |
| A is less than 10                         | NUMERIC          |
| A is less than or equal to 10             | NUMERIC          |
| A is greater than 15                      | NUMERIC          |
| A is greater than or equal to 15          | NUMERIC          |

Example profiles may include:

| Constraint 1                       | Constraint 2          | Valid   | Reason                                                                                                    |
|------------------------------------|-----------------------|---------|-----------------------------------------------------------------------------------------------------------|
| A is of type STRING                | A is of type NUMERIC  | Invalid | A is already set to be of type STRING by Constraint 1. The type cannot be changed to a different one.     |
| A is less than 10                  | A is of type TEMPORAL | Invalid | Constraint 1 implies that A must be of NUMERIC type. The type cannot be changed to a different one.       |
| A is of type STRING                | A is granular to 1    | Invalid | A is of type String and applying granular to constraint is only allowed on NUMERIC fields.                |
| A is after 2008-09-15T15:53:00.000 | A is less than 10     | Invalid | Constraint 1 implies that A must be of TEMPORAL type. Constraint 2 can only be applied to NUMERIC types.  |


## Set restrictions

The profile supports in set and not in set constraints which modify a whitelist / blacklist for a field. The values in the whitelist are allowed values for the field whilst the values in the blacklist are not allowed.


| Constraint                          |     Set Whitelist     |       Set Blacklist |
|-------------------------------------|:---------------------:|--------------------:|
| B is in set [1,2,3]                 |        [1,2,3]        |                     |
| B is in set ["Hello", "Good day"]   | ["Hello", "Good day"] |                     |
| B is not in set ["Bye", "Good bye"] |                       | ["Bye", "Good bye"] |

Example profiles may include:

| Constraint 1                          |        Constraint 2       |   Valid | Reason                                                                                                      |
|---------------------------------------|:-------------------------:|--------:|-------------------------------------------------------------------------------------------------------------|
| B is in set [1,2,3]                   |      B is in set [6]      | Invalid | B is in set [1,2,3]. Constraint 2 tries to define B as in set [6] which is outside of [1,2,3]               |
| B is in set [1,2,3]                   | B is not in set ["Hello"] |   Valid | B is in set [1,2,3]. B not being in set ["Hello"] does not contradict Constraint 1.                         |
| B is not in set ["Hello", "Good day"] |    B is in set ["Bye"]    |   Valid | B is not in set ["Hello", "Good day"]. B not being in this set does not contradict it being in set ["Bye"]. |

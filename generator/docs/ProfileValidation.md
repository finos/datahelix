# Profile Validation

The profile validator analyzes a profile to check if it is valid. A valid profile is one that can generate data. When an invalid profile is observed, the process will terminate and notify the user of the validation errors in the profile. 

The validation checks currently performed include:

1. All data values are excluded from the possible range

    Example: A is in set [3] and A is not in set [3] - there are no values that A can be.

2. Constraint for a different data type is applied

    Example: A is a string and granularity constraint is applied - granularity constraint can only operate on decimal fields at the moment.


The workflow for the profile validator is that it iterates over all the constraints in the profile and for each field it keeps a running collection of restrictions that must be observed in order for the profile to remain valid. When an invalidating constraint is encountered, a validation alert is raised and the restrictions for this field remain unchanged. All validation alerts for all fields will be reported at the end of the process.

Validation alerts have different levels:

| Criticality |      Data generation      |                                                                                                     Explanation |
|-------------|:-------------------------:|-----------------------------------------------------------------------------------------------------------------:|
| Error       | Data cannot be generated. |                       There are unrecoverable contradictions between constraints. These errors must be resolved. |
| Information |   Data can be generated.  | There are constraints that look like they may sometimes contradict but there are some possible values that satisfy them. |

There are a number of checks performed depending on the constraint applied. Below we explain them.


## Type validation

Type validation is performed to ensure that the field type is set correctly and not contradicted by other constraints.

Example profiles may include:

| Constraint 1                       | Constraint 2          | Validity - Logging   | Reason                                                                                                    |
|------------------------------------|-----------------------|---------|-----------------------------------------------------------------------------------------------------------|
| A is of type STRING                | A is of type INTEGER  | Invalid - Error | A is already set to be of type STRING by Constraint 1. The type cannot be changed to a different one.     |
| A is less than 10                  | A is of type DATETIME | Invalid - Error | Constraint 1 implies that A must be of INTEGER or DECIMAL type. The type cannot be changed to a different one.       |
| A is of type STRING                | A is granular to 1    | Invalid - Error | A is of type String and applying granular to constraint is only allowed on DECIMAL fields.                |
| A is after 2008-09-15T15:53:00.000 | A is less than 10     | Invalid - Error | Constraint 1 implies that A must be of DATETIME type. Constraint 2 can only be applied to INTEGER or DECIMAL types.  |


## Set validation
Set validation is performed to ensure there always is a value that can be generated for a field.

Example profiles may include:

| Constraint 1                          |        Constraint 2       |    Validity - Logging | Reason                                                                                                      |
|---------------------------------------|:-------------------------:|--------:|-------------------------------------------------------------------------------------------------------------|
| B is in set [1,2,3]                   |      B is in set [6]      | Invalid - Error | B is in set [1,2,3]. Constraint 2 tries to define B as in set [6] which is outside of [1,2,3]               |
| B is in set [1,2,3]                   | B is not in set ["Hello"] |   Valid - N/A | B is in set [1,2,3]. B not being in set ["Hello"] does not contradict Constraint 1.                         |
| B is not in set ["Hello", "Good day"] |    B is in set ["Bye"]    |   Valid - N/A | B is not in set ["Hello", "Good day"]. B not being in this set does not contradict it being in set ["Bye"]. |
| B is in set ["Hello", "Good day"] |    B is in set ["Hello", "Bye"]    |   Valid - N/A | B is in set ["Hello", "Good day"] from Constraint 1. We have an overlap of the two sets with "Hello" so this is a valid profile.  |


## Numeric validation

Numeric validation is performed to ensure there always is a value that can be generated for a field.

Example profiles may include:

| Constraint 1                     |          Constraint 2         |  Validity - Logging | Reason                                                                                  |
|----------------------------------|:-----------------------------:|------:|-----------------------------------------------------------------------------------------|
| C is greater than 10             | C is less than 20            | Valid - N/A | C is between 10 and 20.                                                                 |
| C is greater than or equal to 10 | C is less than or equal to 10 | Valid - N/A | C is equal to 10.                                                                       |
| C is greater than 10             | C is less than 5              | Valid - Information | Null satisfies these conditions. Null will be provided in all cases. |


## DateTime validation

DateTime validation is performed to ensure there always is a value that can be generated for a field.

Example profiles may include:

| Constraint 1                        |            Constraint 2            |     Valid - Logging | Reason                                                                                  |
|-------------------------------------|:----------------------------------:|--------------------:|-----------------------------------------------------------------------------------------|
| D is before 2000-09-15T15:53:00.000 | D is after 2010-09-15T15:53:00.000 | Valid - Information | Null satisfies these conditions. Null will be provided in all cases. |
| D is before 2000-09-15T15:53:00.000 | D is after 1990-09-15T15:53:00.000 |         Valid - N/A | D is between 1990-09-15T15:53:00.000 and 2000-09-15T15:53:00.000                        |


## Granularity validation

Granularity validation is performed to ensure granularity is set correctly and to highlight the selection of the granularity that will be used.

Example profiles may include:

| Constraint 1          | Constraint 2          |                           Validity - Logging                         |                                                                        Reason |
|-----------------------|-----------------------|:------------------------------------------------------:|------------------------------------------------------------------------------:|
| E is granular to 0.1  | E is granular to 0.01 |                          Valid - Information                         | Both granularities are valid and so the smallest one (0.01) will be selected. |
| E is granular to 0.01 | E is granular to 0.01 |                          Valid - N/A                         |           Both granularities are valid and the same so 0.01 will be selected. |
| E is granular to 1e-8 | E is granular to 1-e6 |                          Valid - Information                         | Both granularities are valid and so the smallest one (1-e8) will be selected. |


## String validation

String validation is performed to ensure there always is a value that can be generated for a field.

Example profiles may include:

| Constraint 1       |     Constraint 2     |  Validity - Logging | Reason                                                                                  |
|--------------------|:--------------------:|------:|-----------------------------------------------------------------------------------------|
| F is longer than 3 |  F is shorter than 3 | Valid - Information | Null satisfies these conditions. Null will be provided in all cases. |
| F is longer than 3 | F is shorter than 10 | Valid - N/A | F is between 4 and 9 characters long.                                                   |


## Null validation

Null validation is performed to ensure there always is a value that can be generated for a field.

Example profiles may include:

| Constraint 1 |  Constraint 2 |    Validity - Logging | Reason                                                                     |
|--------------|:-------------:|--------:|----------------------------------------------------------------------------|
| G is null    | G is not null | Invalid - Error | G cannot be null and not null simultaneously. No results can be generated. |

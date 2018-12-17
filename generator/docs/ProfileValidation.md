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
| Information |   Data can be generated.  | There are constraints that look like they may sometimes contradict but there are some possible values that satisfy them. |

There are a number of checks performed depending on the constraint applied. Below we explain them.

## Type validation

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

| Constraint 1                       | Constraint 2          | Validity - Logging   | Reason                                                                                                    |
|------------------------------------|-----------------------|---------|-----------------------------------------------------------------------------------------------------------|
| A is of type STRING                | A is of type NUMERIC  | Invalid - Error | A is already set to be of type STRING by Constraint 1. The type cannot be changed to a different one.     |
| A is less than 10                  | A is of type TEMPORAL | Invalid - Error | Constraint 1 implies that A must be of NUMERIC type. The type cannot be changed to a different one.       |
| A is of type STRING                | A is granular to 1    | Invalid - Error | A is of type String and applying granular to constraint is only allowed on NUMERIC fields.                |
| A is after 2008-09-15T15:53:00.000 | A is less than 10     | Invalid - Error | Constraint 1 implies that A must be of TEMPORAL type. Constraint 2 can only be applied to NUMERIC types.  |


## Set validation

The profile supports in set and not in set constraints which modify a whitelist / blacklist for a field. The values in the whitelist are allowed values for the field whilst the values in the blacklist are not allowed.


| Constraint                          |     Set Whitelist     |       Set Blacklist |
|-------------------------------------|:---------------------:|--------------------:|
| B is in set [1,2,3]                 |        [1,2,3]        |                     |
| B is in set ["Hello", "Good day"]   | ["Hello", "Good day"] |                     |
| B is not in set ["Bye", "Good bye"] |                       | ["Bye", "Good bye"] |

Example profiles may include:

| Constraint 1                          |        Constraint 2       |    Validity - Logging | Reason                                                                                                      |
|---------------------------------------|:-------------------------:|--------:|-------------------------------------------------------------------------------------------------------------|
| B is in set [1,2,3]                   |      B is in set [6]      | Invalid - Error | B is in set [1,2,3]. Constraint 2 tries to define B as in set [6] which is outside of [1,2,3]               |
| B is in set [1,2,3]                   | B is not in set ["Hello"] |   Valid - N/A | B is in set [1,2,3]. B not being in set ["Hello"] does not contradict Constraint 1.                         |
| B is not in set ["Hello", "Good day"] |    B is in set ["Bye"]    |   Valid - N/A | B is not in set ["Hello", "Good day"]. B not being in this set does not contradict it being in set ["Bye"]. |


## Numeric validation

The profile supports several numeric operations inclusing: is less than, is greater than, is less than or equal to, is greater than or equal to.
Usage of numeric constrains implies that the Type of the field is NUMERIC.


| Constraint                      |  Numeric range  | Field Type |
|---------------------------------|:---------------:|-----------:|
| C is less than 10               | any number < 10 |    NUMERIC |
| C is greater than 10            | any number > 10 |    NUMERIC |
| C is less than or equal to 5    | any number <= 5 |    NUMERIC |
| C is greater than or equal to 5 | any number >= 5 |    NUMERIC |

Example profiles may include:

| Constraint 1                     |          Constraint 2         |  Validity - Logging | Reason                                                                                  |
|----------------------------------|:-----------------------------:|------:|-----------------------------------------------------------------------------------------|
| C is less than 10                | C is great than 20            | Valid - N/A | C is between 10 and 20.                                                                 |
| C is greater than or equal to 10 | C is less than or equal to 10 | Valid - N/A | C is equal to 10.                                                                       |
| C is greater than 10             | C is less than 5              | Valid - Information | The empty set satisfies these conditions. An empty value will be provided in all cases. |


## Temporal validation
The profile supports several temporal operations inclusing: is after, is before, is after or at, is before or at.
Usage of temporal constraints implies that the Type of the field is TEMPORAL.


| Constraint                                |                   Temporal range                  | Field Type |
|-------------------------------------------|:-------------------------------------------------:|-----------:|
| D is before 2000-09-15T15:53:00.000       | any datetime before 2000-09-15T15:53:00.000       | TEMPORAL   |
| D is after 2000-09-15T15:53:00.000        | any datetime after 2000-09-15T15:53:00.000        | TEMPORAL   |
| D is before or at 2008-09-15T15:53:00.000 | any datetime before or at 2008-09-15T15:53:00.000 | TEMPORAL   |
| D is after or at 2008-09-15T15:53:00.000  | any datetime after or at 2008-09-15T15:53:00.000  | TEMPORAL   |

Example profiles may include:

| Constraint 1                     |          Constraint 2         |  Validity - Logging | Reason                                                                                  |
|----------------------------------|:-----------------------------:|------:|-----------------------------------------------------------------------------------------|
| C is less than 10                | C is great than 20            | Valid - N/A | C is between 10 and 20.                                                                 |
| C is greater than or equal to 10 | C is less than or equal to 10 | Valid - N/A | C is equal to 10.                                                                       |
| C is greater than 10             | C is less than 5              | Valid - Information | The empty set satisfies these conditions. An empty value will be provided in all cases. |


## Granularity validation

The profile supports the specification of a granularity which must be a positive number, less than 1 and a fractional power of 10.
Usage of granularity constraints implies that the Type of the field is NUMERIC. 


| Constraint            |                       Granularity                      | Field Type |
|-----------------------|:------------------------------------------------------:|-----------:|
| E is granular to 0.1  |  E is a number with up to 1 decimal place granularity. |    NUMERIC |
| E is granular to 0.01 | E is a number with up to 2 decimal places granularity. |    NUMERIC |
| E is granular to 1e-8 |  E is a number with up to 8 decimal place granularity. |    NUMERIC |

Example profiles may include:

| Constraint 1          | Constraint 2          |                           Validity - Logging                         |                                                                        Reason |
|-----------------------|-----------------------|:------------------------------------------------------:|------------------------------------------------------------------------------:|
| E is granular to 0.1  | E is granular to 0.01 |                          Valid - Information                         | Both granularities are valid and so the smallest one (0.01) will be selected. |
| E is granular to 0.01 | E is granular to 0.01 |                          Valid - N/A                         |           Both granularities are valid and the same so 0.01 will be selected. |
| E is granular to 1e-8 | E is granular to 1-e6 |                          Valid - Information                         | Both granularities are valid and so the smallest one (1-e6) will be selected. |


## String validation

The profile supports several constraints on strings length including: is shorter than, is longer than.
Usage of string constraints implies that the Type of the field is STRING.

| Constraint          |              String length | Field Type |
|---------------------|---------------------------:|------------|
| F is longer than 3  | F is 4 characters or more. | STRING     |
| F is shorter than 5 | F is 4 characters or less. | STRING     |

Example profiles may include:

| Constraint 1       |     Constraint 2     |  Validity - Logging | Reason                                                                                  |
|--------------------|:--------------------:|------:|-----------------------------------------------------------------------------------------|
| F is longer than 3 |  F is shorter than 3 | Valid - Information | The empty set satisfies these conditions. An empty value will be provided in all cases. |
| F is longer than 3 | F is shorter than 10 | Valid - N/A | F is between 4 and 9 characters long.                                                   |


## Null validation

The profile supports is null and not is null constraints. 

| Constraint    |     Nullness     |
|---------------|:----------------:|
| G is null     |   Must be null   |
| G is not null | Must not be null |

Example profiles may include:

| Constraint 1 |  Constraint 2 |    Validity - Logging | Reason                                                                     |
|--------------|:-------------:|--------:|----------------------------------------------------------------------------|
| G is null    | G is not null | Invalid - Error | G cannot be null and not null simultaneously. No results can be generated. |

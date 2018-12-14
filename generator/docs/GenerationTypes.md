# Generation types

The data generator supports the following data generation types

* Full sequential
* Random
* Interesting

## Full sequential
Generate every possible value for the given set of constraints. This mode could cause the tool to run forever unless some other conditions are applied, e.g. max number of rows.

Examples:

| Constraint | Emitted valid data | Emitted violating data |
| ---- | ---- | ---- |
| `Field 1 > 10 AND Field 1 < 20` | 11, 12, 13, 14, 15, 16, 17, 18, 19 | _(all values <= 10)_, _(all values >= 20)_ |
| `Field 1 in set [A, B, C]` | A, B, C | `null` |

## Random
Generate some random data that abides by the given set of constraints. This mode could cause the tool to run forever unless some other conditions are applied, e.g. max number of rows. This mode has the potential to repeat data points, it does not keep track of values that have already been emitted.

Examples:

| Constraint | Emitted valid data |Emitted violating data |
| ---- | ---- | ---- |
| `Field 1 > 10 AND Field 1 < 20` | _(any values > 10 & < 20)_ | _(any values <= 10 or >= 20)_ |
| `Field 1 in set [A, B, C]` | _(A, B or C in any order, repeated as needed)_ | `null` |

## Interesting
Generate some data that matches the boundary conditions for the given set of constraints. This mode will also emit data that is required to satisfy conditions in other constraints (e.g. `IF` constraints).

Examples:

| Constraint | Emitted valid data |Emitted violating data |
| ---- | ---- | ---- |
| `Field 1 > 10 AND Field 1 < 20` | 11, 19 | 10, 20 |
| `Field 1 in set [A, B, C]` | A, B, C* | `null` |

\* This may change in future.
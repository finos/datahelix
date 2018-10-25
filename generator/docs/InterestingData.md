# Interesting data

The generator - initially - will run in 'interesting' mode which will emit rows of data that match the boundary conditions whether in valid or violating mode.

Without this option the tool will have to:
1. Exhaustively produce data for the constraint, which could produce an infinite set.
2. Produce random data _OR_ produce a limited set of data, each of which could miss out critical values.

## Examples

| Constraint | Valid examples | Violating examples |
| -- | -- | -- |
| String, length 5-8 | "abcde", "abcdefgh" | "1234", "123456789", `null`, "", "\0" |
| Integer, 5 < x < 10 | 6, 9 | 5, 10, `null`, 0, -1 |

These examples demonstrate that the invalid/violating mode will produce a larger set of results than the valid case; but that there are certain critical cases that must be emitted; e.g. `null`, `""` and `0`.

For the full set of valid and invalid interesting values, see the test spreadsheet and the cucumber tests.

## Complications

* How to produce invalid/violating interesting data from a regular expression (e.g. `[A-Z]{2}\d{10}` - the regex for an ISIN).
* How to know whether temporal violating interesting data contains times in the past, future or both - e.g. 'Transaction date', 'Account closure date'.
* How to produce invalid/violating interesting data for different character sets (e.g. ASCII, Unicode, etc.)
# Selective violation
_This is an alpha feature. Please do not rely on it. If you find issues with it, please [report them](https://github.com/finos/datahelix/issues)._ 

The current selective violations allows a user to choose an operator/type of constraint to not violate.
All of the constraints of that type will not be not be violated in the entire profile.

e.g. using the command line argument `--dont-violate=lessThan`
will mean that every single less than constraint will not be violated.

Selective violation does nothing if the generator is not run in violation mode.

## Limitations
- `equalsTo` and `inSet` are considered the same. So choosing not to violate either, will make the system not violate both.
- Can't choose to not violate grammatical constraints
- If all constraints in a profile are selected to be not violated, the system will generate valid data

## Potential future work
- Let user choose single constraints or rules to violate.
- Let user choose single constraints or rules to not violate.

# Generate Options
Option switches are case-sensitive, arguments are case-insensitive

* `--overwrite`
    * Overwrite existing output files.
* `--violate`
   * Generate data which violates profile constraints.
* `--dont-violate` <epistemic constraints...>
   * Choose specific [epicstemic constraints](../EpistemicConstraints.md) to [not violate](../generator/docs/SelectiveViolation.md), e.g. "--dont-violate=typeOf lessThan" will not violate ANY data type constraints and will also not violate ANY less than constraints.
* `-n <rows>` or `--max-rows <rows>`
   * Emit at most `<rows>` rows to the output file, if not specified will limit to 10,000,000 rows.
   * Mandatory in `RANDOM` mode.
* `--no-partition`
   * Prevent rules from being partitioned during generation. Partitioning allows for a (unproven) performance improvement when processing larger profiles.
* `--no-optimise`
   * Prevent profiles from being optimised during generation. Optimisation enables the generator to process profiles more efficiently, but adds more compute in other areas. See [Decision tree optimiser](../../generator/docs/OptimisationProcess.md) for more details.
* `--validate-profile`
   * Validate the profile, check to see if known [contradictions](../../generator/docs/Contradictions.md) exist, see [Profile validation](../../generator/docs/ProfileValidation.md) for more details.

By default the generator will report how much data has been generated over time, the other options are below:
* `--verbose`
    * Will report in-depth detail of data generation
* `--quiet`
    * Will disable velocity reporting
    
`--quiet` will be ignored if `--verbose` is supplied.

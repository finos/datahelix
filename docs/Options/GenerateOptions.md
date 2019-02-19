# Generate Options
Option switches are case-sensitive, arguments are case-insensitive

* `--overwrite`
    * Overwrite existing output files. Options are: `true` or `false` (default)
* `--violate`
   * Generate data which violates profile constraints.
* `--dont-violate` <epistemic constraints...>
   * Choose specific [epicstemic constraints](../EpistemicConstraints.md) to [not violate](../generator/docs/SelectiveViolation.md), e.g. "--dont-violate=typeOf lessThan" will not violate ANY data type constraints and will also not violate ANY less than constraints.
* `-t <generationType>` or `--generation-type <generationType>`
   * Emit `<generationType>` data. Options are: `INTERESTING` (default) or `RANDOM`, `FULL_SEQUENTIAL`, see [Generation types](../../generator/docs/GenerationTypes.md) for more details.
* `-n <rows>` or `--max-rows <rows>`
   * Emit at most `<rows>` rows to the output file, if not specified will limit to 10,000,000 rows.
   * Mandatory in `RANDOM` mode.
* `-c <combinationType>` or `--combination-strategy <combinationType>`
   * When producing data combine each data point using the `<combinationType>` strategy. Options are: `PINNING` (default), `EXHAUSTIVE`, `MINIMAL`, see [Combination strategies](../../generator/docs/CombinationStrategies.md) for more details.
* `-w <walker>` or `--walker-type <walker>`
   * Use `<walker>` strategy for producing data. Options are: `CARTESIAN_PRODUCT`, `ROUTED`, `REDUCTIVE` (default), see [Tree walker types](../../generator/docs/TreeWalkerTypes.md) for more details.
* `--no-partition`
   * Prevent rules from being partitioned during generation. Partitioning allows for a (unproven) performance improvement when processing larger profiles.
* `--no-optimise`
   * Prevent profiles from being optimised during generation. Optimisation enables the generator to process profiles more efficiently, but adds more compute in other areas. See [Decision tree optimiser](../../generator/docs/OptimisationProcess.md) for more details.
* `--validate-profile`
   * Validate the profile, check to see if known [contradictions](../../generator/docs/Contradictions.md) exist, see [Profile validation](../../generator/docs/ProfileValidation.md) for more details.
* `--trace-constraints`
   * When generating data emit a `<output path>-trace.json` file which will contain details of which rules and constraints caused the generator to emit each data point. See [Tracing Output](../../generator/docs/TracingOutput.md) for more details.
* `--visualise-reductions`
   * Debug mode for the generator. Turn on per-reduction visualisation of the profile decision tree. Will create a directory named `reductive-walker` under the output path for the files.

By default the generator will report how much data has been generated over time, the other options are below:
* `--verbose`
    * Will report in-depth detail of data generation
* `--quiet`
    * Will disable velocity reporting
    
`--quiet` will be ignored if `--verbose` is supplied.

# Generate Options
Option switches are case-sensitive, arguments are case-insensitive

* `--replace`
    * Overwrite/replace existing output files.
* `-n <rows>` or `--max-rows <rows>`
   * Emit at most `<rows>` rows to the output file, if not specified will limit to 10,000,000 rows.
   * Mandatory in `RANDOM` mode.
* `--validate-profile`
   * Validate the profile, check to see if known [contradictions](../../generator/docs/Contradictions.md) exist, see [Profile validation](../../generator/docs/ProfileValidation.md) for more details.
* `-o <output-format>`
   * Output the data in the given format, either CSV (default) or JSON.
   * Note that JSON format requires that all data is held in-memory until all data is known, at which point data will be flushed to disk, this could have an impact on memory and/or IO requirements

By default the generator will report how much data has been generated over time, the other options are below:
* `--verbose`
    * Will report in-depth detail of data generation
* `--quiet`
    * Will disable velocity reporting
    
`--quiet` will be ignored if `--verbose` is supplied.

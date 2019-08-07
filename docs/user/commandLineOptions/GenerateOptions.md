# Generate Options
Option switches are case-sensitive, arguments are case-insensitive

* `--profile-file=<path>` (or `-p <path>`)
    * Path to the input profile file.
* `--output-path=<path>` (or `-o <path>`)
    * Path to the output file.  If not specified, output will be to standard output.
* `--replace`
    * Overwrite/replace existing output files.
* `-n <rows>` or `--max-rows <rows>`
   * Emit at most `<rows>` rows to the output file, if not specified will limit to 10,000,000 rows.
   * Mandatory in `RANDOM` mode.
* `--disable-schema-validation`
   * Generate without first checking profile validity against the schema. This can be used if you believe the schema is incorrectly rejecting your profile.
* `-o <output-format>`
   * Output the data in the given format, either CSV (default) or JSON.
   * Note that JSON format requires that all data is held in-memory until all data is known, at which point data will be flushed to disk, this could have an impact on memory and/or IO requirements
* `--allow-untyped-fields`
    * Turns off type checking on fields in the profile.

By default the generator will report how much data has been generated over time, the other options are below:
* `--verbose`
    * Will report in-depth detail of data generation
* `--quiet`
    * Will disable velocity reporting
    
`--quiet` will be ignored if `--verbose` is supplied.

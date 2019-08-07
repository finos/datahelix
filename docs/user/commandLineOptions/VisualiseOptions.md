# Visualise Options
Option switches are case-sensitive, arguments are case-insensitive

* `--profile-file=<path>` (or `-p <path>`)
   * Path to input profile file.
* `--output-path=<path>` (or `-o <path>`)
   * Path to visualisation output file.
* `-t <title>` or `--title <title>`
   * Include the given `<title>` in the visualisation. If not supplied, the description of in profile will be used, or the filename of the profile.
* `--no-title`
   * Exclude the title from the visualisation. This setting overrides `-t`/`--title`.
* `--replace`
   * Overwrite/replace existing output files.
* `--allow-untyped-fields`
   * Turns off type checking on fields in the profile.

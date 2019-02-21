# Visualise Options
Option switches are case-sensitive, arguments are case-insensitive

* `-t <title>` or `--title <title>`
   * Include the given `<title>` in the visualisation. If not supplied, the description of in profile will be used, or the filename of the profile.
* `--no-title`
   * Exclude the title from the visualisation. This setting overrides `-t`/`--title`.
* `--no-optimise`
   * Prevents tree optimisation during visualisation.
* `--no-simplify`
   * Prevents tree simplification during visualisation. Simplification is where decisions with only one option are folded into the parent.

# Expected output files for profiler

This folder keeps a few expected output JSON files that are generated based on the input CSV files kept in profiler/src/test/resources/

Potentially we can use these files as regression information to keep track of the changes that will be made to the output results of profilers.  WARNING: Note that until we have a continuous integration (CI) system that allows us to always generate new expected output files as our code changes, currently these files are regenerated manually and may go stale when the regeneration is not done.

## How to regenerate these files

Please refer to the usage information currently defined in `App.scala`  on how to invoke the main program.

As of now the program takes two arguments, a path to an input file (which we keep in profiler/src/test/resources/) and a path to an output folder (you can set it to this folder).
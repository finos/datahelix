# Expected output files for profiler

This folder keeps a few expected output JSON files that are generated based on the input CSV files kept in profiler/src/test/resources/

The files are used for integration testing the profiler. The tests are ran as part of the build process and check if the profiles generated based on the input CSV files still look exactly like the expected profiles.

The tests can be found in the following location

- `profiler\src\test\scala\com.scottlogic.deg\`


## How to regenerate these files or generate new files

Please refer to the usage information currently defined in `App.scala`  on how to invoke the main program.

As of now the program takes two arguments, a path to an input file (which we keep in profiler/src/test/resources/) and a path to an output folder (you can set it to this folder).
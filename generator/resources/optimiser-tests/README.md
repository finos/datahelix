# Tree Optimiser Integration Tests

This directory keeps test files for integration tests for the tree optimiser.  Each test is performed by comparing an in-memory decision tree after optimisation step (the actual result) and an in-memory expected decision tree as loaded from a `expected.json` file.

Each directory describes a single test and the name of that directory will also appear in the test runner. Each directory should have a `profile.json` file and an `expected.json` file.  It may also optionally have a `README.md` file to explain the purpose of the test.

## `profile.json`

The `profile.json` has the same format as that used by the generator. The tester will load the file and then optimises it and produce an in-memory tree which is the "actual" side of the test.

## `expected.json`

The `expected.json` file represents the "expected" side of the test and describes the expected state of the in-memory decision trees after optimisation.

You can manually create the `expected.json` files, based on your understanding of the expected state of the in-memory tree after optimisation.  But if you have a known good profile representing a profile after optimisation, you may also use the `genTreeJson` mode of the generator as a tool to help you create the `expected.json` file.
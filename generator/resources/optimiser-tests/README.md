# Tree Optimiser Integration Tests

This directory keeps test files for integration tests for the tree optimiser.  Each test is performed by comparing an in-memory decision tree after optimisation step (the actual result) and an in-memory expected decision tree as loaded from a `expected.json` file.

Each directory describes a single test and the name of that directory will also appear in the test runner. Each directory should have a `profile.json` file and an `expected.json` file.  It may also optionally have a `README.md` file to explain the purpose of the test.

## `profile.json`

The `profile.json` has the same format as that used by the generator. The tester will load the file and then optimises it and produce an in-memory tree which is the "actual" side of the test.

## `expected.json`

The `expected.json` file represents the "expected" side of the test and describes the expected state of the in-memory decision trees after optimisation.

Note that since the tests for the Optimiser and the Tree Partitioner share the same code, and the latter has a collection of trees (after partitioning) in each expected result, we also make each `expected.json` file here stores a collection of trees, but there is always only one member in the collection.

You can manually create the `expected.json` files, based on your understanding of the expected state of the in-memory tree after optimisation.  But if you have a known good profile representing a profile after optimisation, you may also use the `genTreeJson` mode of the generator as a tool to help you create the `expected.json` file.
# Tree Partitioner Integration Tests

This directory is used to test the tree partitioner by comparing the output of a partitioned tree from a valid profile against an expected partitioned tree generated from an expected model.

Each directory describes a single test and the name of that directory will also appear in the test runner. Each directory should have a profile.json file, an expected-partitioning.json file and a README file to explain the purpose of the test.

## profile.json

The profile.json file works the same way that an ordinary profile file would when running against the generator. This is used to generate a real partitioned tree and can be thought of as the actual value in a test.

## expected-partitioning.json

The expected-partitioning.json file is used to model the partitioned tree in the generator and can be thought of as the expected side of the test. The JSON is designed to model the partitioned tree object almost exactly with one difference: The type field is used for the Jackson library so that the type can be mapped to the correct DTO. The order of the trees, decision and atomic constraints are unimportant for testing the comparison of the expected with the actual.
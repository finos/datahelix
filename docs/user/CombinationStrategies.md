# Combination strategies

In multiple areas of the generator, we find ourselves with multiple streams of valid values for individual fields, and need to select combinations of them for output as data rows. For example:

* Field A can be X or Y
* Field B can be 1, 2 or 4

There are multiple ways to perform this selection process; we refer to these as **combination strategies**. Different combination strategies have different properties.

Our current default is the **exhaustive** strategy.

## Exhaustive

The exhaustive strategy outputs all possible combinations. Given the fields as defined above, possible outputs would be:

* X, 1
* X, 2
* X, 4
* Y, 1
* Y, 2
* Y, 4

It has these properties:

* Combines every value with every other value
* It is always possible to find another output differing by just one field
* Output size increases **exponentially** with number of fields

## Minimal

The minimal strategy outputs the minimum data required to exemplify each value at least once. Per the example, possible outputs would be:

* X, 1
* Y, 2
* Y, 4

It has these properties:

* Exemplifies each value at least once
* Output size increases **linearly** with number of fields

## Pinning

The pinning strategy establishes a baseline for each field (generally by picking the first available value for that field) and then creates outputs such that either:

* All values equal the baseline for the respective field
* All values except one equal the baseline for the respective field

To generate these outputs, we first output the first case (all values from baseline) and then iterate through each field, F, fixing all other fields at their baseline and generating the full range of values for F. For the example, possible outputs would be:

* X, 1  *(all baselines)*
* Y, 1  *(looping through values for first field)*
* X, 2  *(looping through values for second field)*
* X, 4  *(looping through values for second field)*

It has these properties:

* Exemplifies each value at least once
* It is always possible to find another output differing by just one field
* Output size increases **linearly** with number of fields
* The maximum number of rows generated can be calculated as: ( the sum of the total number of possible values for all fields ) less ( the total number of fields ) plus 1

## Random

Whereas other strategies produce a bounded set of outputs, the random strategy produces an infinite series by repeatedly randomly picking values for each field.

## Possible future strategies

### Hybrid

We may want to be exhaustive over some fields (because the user thinks there may be unknown interactions between them) but minimal over others.

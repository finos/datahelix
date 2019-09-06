# DataHelix Generator [![CircleCI](https://circleci.com/gh/finos/datahelix.svg?style=svg)](https://circleci.com/gh/finos/datahelix) [![FINOS - Incubating](https://cdn.jsdelivr.net/gh/finos/contrib-toolbox@master/images/badge-incubating.svg)](https://finosfoundation.atlassian.net/wiki/display/FINOS/Incubating)

![DataHelix logo](docs/logo.png)

The generation of representative test and simulation data is a challenging and time-consuming task. The DataHelix generator allows you to quickly create data, based on a JSON profile that defines fields and the relationships between them, for the purpose of testing and validation. The generator supports a number of generation modes, allowing the creation of data that both conforms to, or violates, the profile.

DataHelix is a proud member of the [Fintech Open Source Foundation](https://www.finos.org/) and operates within the [Data Technologies Program](https://www.finos.org/dt).

For information on how to get started with DataHelix see our [Getting Started guide](docs/GettingStarted.md) and for information on the syntax of the DataHelix schema see the [User Guide](docs/user/UserGuide.md). If you would like information on how to contribute to the project as well as a technical overview of the key concepts and structure of the DataHelix then see the [Developer Guide](docs/DeveloperGuide.md).

For a high level road map see [Road Map](docs/RoadMap.md).

## The Problem
When performing functional or load testing on a system, prototyping an API or pitching a service to a potential customer, sample data is mandatory, but maintaining it is often expensive and error prone. The nature of financial services makes it particularly difficult to cleanly manage data schemas, and therefore sample datasets:

* Regulatory and methodological change often forces changes in how data is represented.
* It is often difficult to completely remove legacy data due to obligations to honour deprecated financial products.
* Errors can be costly and reputation-damaging, which strongly disincentivises the large-scale code change that comes with schema reinvention.
* For legal and/or privacy reasons, it is often impossible to include real data in samples.

The usual response is to progressively complicate schemas with special cases and exceptions, such as:

* *"We introduced a subtype of a product. We couldn't rewrite the `product_code` field's legal values, so we introduced a `sub_product_code` field that should only be populated when `product_code` is a specific value"*
* *"We realised that not all trades will have an `action_date`, but our new lead dislikes nulls, so we're using `N/A` for those trades. We'll have to change the field type to string and accept that invalid dates could creep in"*

For all the above reasons, it is common to handcraft sample datasets. This approach brings several problems:

* It costs significant time up-front, and thereafter every time the schema changes.
* It's very easy to introduce errors, especially when updating data created by others.
* It can be difficult to determine how exhaustively the data covers valid cases, especially when complicated multi-field rules are involved.

Moreover, since the schema's complexities are only loosely documented, and not directly integrated into any systems that use or create the data, there is no guard against the documentation becoming out of date and inaccurate. It would be better if the mechanism for documenting the data were also used in its generation and validation.

For data generation, partial solutions are available in services/libraries such as TSimulus, Mockaroo or GenRocket. However, these have limitations:

* They are limited to relatively simple data schemas, with either no cross-field dependencies or dependencies limited to pairs of fields.
* None of them offer a complete end-to-end solution of profiling existing data to discover trends and rules, generating from those rules, and validating against them.
* Where they offer complicated behaviour, it's through an *imperative* style, forcing the user to design solutions using the library's toolbox. Our approach has a *declarative* style, where the user describes their data and the system decides how to deliver it. This means users don't need to have more than surface-level understanding to write profiles, opening up the scope of who can write or maintain them.

## The Mission

We aim to solve (at least) the following user needs:
- "I want to generate test cases for my validation procedures"
- "I want to generate volumes of sample data to document my API, or to return from a non-production version of my API"
- "I want to validate some data against known specifications"
- "I want to measure my existing test data's coverage against the range of possible data"
- "I want to generate a combinatorially exhaustive set of data, for testing an API's robustness"

## The Product
A suite of tools:
- That can profile data it is pointed at, including constraints and rules, or take in a profile created via a web front end/manually, which defines a users needs.
- That can generate data from this profile via web page, CLI or Rest API interface, configured for volume and output needs.
- That can validate the data produced back against the profile used to create it

## License

Copyright 2019 Scott Logic Ltd

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)

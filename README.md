(To be updated: Badges go here (see [shields.io](https://shields.io/), for examples).)

# Data Generator

This monorepo contains the different technological arms of Scott Logic's Data Generator project, which allows users to automatically analyse their real data to create a profile, and then use that profile to generate realistic dummy data.

## Installation

(To be added)

## Usage example

(To be added: A few motivating and useful examples of how your project can be used. Spice this up with code blocks and potentially screenshots / videos ([LiceCap](https://www.cockos.com/licecap/) is great for this kind of thing).

## Documentation

* [Data profiles](docs/Profiles.md)

## Development setup

The `profiler` module contains Scala code so you have to first set up your IDE (IntelliJ IDEA or
Eclipse) to support Scala.  It also depends on Spark and Hadoop.  Please refer to `profiler/README.md` for the details.

And then you can import the top level working directory `data-engineering-generator` into your IDE as an Maven project (In Eclipse: File > Import... > Maven > Existing Maven Project).  Four projects will be imported as a result

* `parent`
* `generator`
* `profiler`
* `schemas` 

## Contributing

(To be added)

## License

Copyright 2018 Scott Logic Ltd

Distributed under the [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

SPDX-License-Identifier: [Apache-2.0](https://spdx.org/licenses/Apache-2.0)

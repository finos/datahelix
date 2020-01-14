### DataHelix

This profile demonstrates some of the features of the DataHelix generator, see the other examples for other ways of using the generator.

This profile will generate rows with a random:
- `execution_time` (the time the generator was run)
- `nyse_stock` (a fake NYSE stock identifier, from Faker)
- `units_held` (a random number (integer) of units held)
- `unit_price` (a fake random price (to 2 decimal places) for each unit)
- `country_of_origin` (a random country name, from Faker)
- `is_open` (_true_ if `open_date` is before `execution_time` AND `close_date` is after `execution_time`, otherwise _false_)
- `open_date` (a random date between  1990 and 2025)
- `close_date` (3 years after `open_date`, or `null`)

Click _Run_ to see some of the data it can produce, or take a look at the other examples to see other types and combinations of data that can be produced.

Produces data like:

| execution_time | nyse_stock | units_held | unit_price | country_of_origin | is_open | open_date | close_date |
| ---- | ---- | ----- | ----- | ---- | ---- | ---- | ----- |
| 2020-01-13T08:13:24.111Z | AGC | 853266 | 854.38 | France | true | 2017-09-13T00:00:00Z | 4961-12-06T00:00:00Z |
| 2020-01-13T08:13:24.111Z | RQI | 421310 | 52.79 | Iran (Islamic Republic of) | false | 1994-02-05T00:00:00Z | 2011-07-28T00:00:00Z |
| 2020-01-13T08:13:24.111Z | DFT | 44466 | 260.83 | Grenada | false | 2016-02-21T00:00:00Z | 2016-03-31T00:00:00Z |
| 2020-01-13T08:13:24.111Z | MSB | 55423 | 639.82 | Cuba | false | 2002-10-30T00:00:00Z | |
| 2020-01-13T08:13:24.111Z | CTB | 673292 | 63.31 | Bolivia (Plurinational State of) | true | 1991-04-16T00:00:00Z | 5170-02-21T00:00:00Z |
| 2020-01-13T08:13:24.111Z | CCC | 477944 | 32.33 | Latvia | false | 2023-09-22T00:00:00Z | 9214-05-06T00:00:00Z |
| 2020-01-13T08:13:24.111Z | PL^C | 7837 | 267.36 | Fiji | true | 2011-03-12T00:00:00Z | |
| 2020-01-13T08:13:24.111Z | PTHN | 936193 | 155.53 | Papua New Guinea | false | 2022-11-23T00:00:00Z | 3437-11-12T00:00:00Z |
| 2020-01-13T08:13:24.111Z | STM | 760765 | 232.45 | Tonga | false | 2019-09-06T00:00:00Z | |
| 2020-01-13T08:13:24.111Z | HCI | 917927 | 278.89 | Latvia | true | 2009-07-20T00:00:00Z | |
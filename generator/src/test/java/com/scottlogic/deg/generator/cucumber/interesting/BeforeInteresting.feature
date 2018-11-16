Feature: User can generate interesting values whilst specifying that a date is lower than, but not equal to, a specified threshold

Background:
     Given the generation strategy is interesting

@ignore
Scenario: User creates data before a specified date
     Given there is a field foo
       And foo is before 2018-10-10T00:00:00.000
     Then the following data should be included in what is generated:
       | foo                     |
       | null                    |
       | 2018-10-09T23:59:59.999 |
       | 2018-10-09T23:00:00.000 |
       | 2018-10-09T00:00:00.000 |
       | 2018-10-03T00:00:00.000 |
       | 2018-09-10T00:00:00.000 |
       | 2017-12-25T00:00:00.000 |
       | 2017-10-10T00:00:00.000 |
       | 2016-02-29T00:00:00.000 |
       | 2000-01-01T00:00:00.000 |
       | 1999-12-31T23:59:59.999 |
       | 1980-01-01T00:00:00.000 |
       | 1978-01-01T00:00:00.000 |
       | 1970-01-01T00:00:00.000 |
       | 1840-01-01T00:00:00.000 |
       | 1601-01-01T00:00:00.000 |
       | 0001-01-01T00:00:00.000 |
     And the following data should not be included in what is generated:
       | foo                     |
       | 2018-10-10T00:00:00.001 |
       | 2018-10-10T01:00:00.000 |
       | 2018-10-10T01:00:00.000 |
       | 2018-10-11T00:00:00.000 |
       | 2018-10-17T00:00:00.000 |
       | 2018-11-10T00:00:00.000 |
       | 2019-10-10T00:00:00.000 |
       | 2020-02-29T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2038-12-19T00:00:00.000 |
       | 2079-06-06T00:00:00.000 |
       | 2999-12-31T23:59:59.999 |
       | 3000-01-01T00:00:00.000 |
       | 9999-12-31T00:00:00.000 |

@ignore
Scenario: User creates data before a specified date and time
     Given there is a field foo
       And foo is before 2018-10-10T23:59:59.999
     Then the following data should be included in what is generated:
       | foo                     |
       | null                    |
       | 2018-10-10T23:59:59.998 |
       | 2018-10-09T23:59:59.999 |
       | 2018-10-09T00:00:00.000 |
       | 2018-10-03T23:59:59.999 |
       | 2018-09-10T23:59:59.999 |
       | 2017-12-25T00:00:00.000 |
       | 2017-10-10T23:59:59.999 |
       | 2016-02-29T00:00:00.000 |
       | 2000-01-01T00:00:00.000 |
       | 1999-12-31T23:59:59.999 |
       | 1980-01-01T00:00:00.000 |
       | 1978-01-01T00:00:00.000 |
       | 1970-01-01T00:00:00.000 |
       | 1840-01-01T00:00:00.000 |
       | 1601-01-01T00:00:00.000 |
       | 0001-01-01T00:00:00.000 |
     And the following data should not be included in what is generated:
       | foo                     |
       | 2018-10-11T00:00:00.000 |
       | 2018-10-11T01:00:00.000 |
       | 2018-10-11T23:59:59.999 |
       | 2018-10-17T23:59:59.999 |
       | 2018-11-10T23:59:59.999 |
       | 2019-10-10T23:59:59.999 |
       | 2020-02-29T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2038-12-19T00:00:00.000 |
       | 2079-06-06T00:00:00.000 |
       | 2999-12-31T23:59:59.999 |
       | 3000-01-01T00:00:00.000 |
       | 9999-12-31T00:00:00.000 |

@ignore
Scenario: User creates data before two specified dates
     Given there is a field foo
       And foo is before 2018-10-10T00:00:00.000
       And foo is before 2018-10-12T00:00:00.000
     Then the following data should be included in what is generated:
       | foo                     |
       | null                    |
       | 2018-10-09T23:59:59.999 |
       | 2018-10-09T23:00:00.000 |
       | 2018-10-09T00:00:00.000 |
       | 2018-10-03T00:00:00.000 |
       | 2018-09-10T00:00:00.000 |
       | 2017-12-25T00:00:00.000 |
       | 2017-10-10T00:00:00.000 |
       | 2016-02-29T00:00:00.000 |
       | 2000-01-01T00:00:00.000 |
       | 1999-12-31T23:59:59.999 |
       | 1980-01-01T00:00:00.000 |
       | 1978-01-01T00:00:00.000 |
       | 1970-01-01T00:00:00.000 |
       | 1840-01-01T00:00:00.000 |
       | 1601-01-01T00:00:00.000 |
       | 0001-01-01T00:00:00.000 |
     And the following data should not be included in what is generated:
       | foo                     |
       | 2018-10-10T00:00:00.001 |
       | 2018-10-10T01:00:00.000 |
       | 2018-10-10T01:00:00.000 |
       | 2018-10-11T00:00:00.000 |
       | 2018-10-17T00:00:00.000 |
       | 2018-11-10T00:00:00.000 |
       | 2019-10-10T00:00:00.000 |
       | 2020-02-29T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2038-12-19T00:00:00.000 |
       | 2079-06-06T00:00:00.000 |
       | 2999-12-31T23:59:59.999 |
       | 3000-01-01T00:00:00.000 |
       | 9999-12-31T00:00:00.000 |

@ignore
Scenario: User creates data that is anything but before a specified date
     Given there is a field foo
       And foo is anything but before 2018-10-10T00:00:00.000
     Then the following data should be included in what is generated:
       | foo                     |
       | null                    |
       | 2018-10-10T00:00:00.001 |
       | 2018-10-10T01:00:00.000 |
       | 2018-10-10T01:00:00.000 |
       | 2018-10-11T00:00:00.000 |
       | 2018-10-17T00:00:00.000 |
       | 2018-11-10T00:00:00.000 |
       | 2019-10-10T00:00:00.000 |
       | 2020-02-29T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2018-12-25T00:00:00.000 |
       | 2038-12-19T00:00:00.000 |
       | 2079-06-06T00:00:00.000 |
       | 2999-12-31T23:59:59.999 |
       | 3000-01-01T00:00:00.000 |
       | 9999-12-31T00:00:00.000 |
     And the following data should not be included in what is generated:
       | foo                     |
       | 2018-10-09T23:59:59.999 |
       | 2018-10-09T23:00:00.000 |
       | 2018-10-09T00:00:00.000 |
       | 2018-10-03T00:00:00.000 |
       | 2018-09-10T00:00:00.000 |
       | 2017-12-25T00:00:00.000 |
       | 2017-10-10T00:00:00.000 |
       | 2016-02-29T00:00:00.000 |
       | 2000-01-01T00:00:00.000 |
       | 1999-12-31T23:59:59.999 |
       | 1980-01-01T00:00:00.000 |
       | 1978-01-01T00:00:00.000 |
       | 1970-01-01T00:00:00.000 |
       | 1840-01-01T00:00:00.000 |
       | 1601-01-01T00:00:00.000 |
       | 0001-01-01T00:00:00.000 |

@ignore
Scenario: User attempts to create data before a specified invalidly formatted date
     Given there is a field foo
       But the profile is invalid as foo can't be before "2018-10-01"
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: User attempts to create data before a specified invalid date
     Given there is a field foo
       But the profile is invalid as foo can't be before 2018-10-40T00:00:00.000
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: User attempts to create data before a specified invalidly formatted time
     Given there is a field foo
       But the profile is invalid as foo can't be before "2018-10-01T01:00:00.000AM"
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: User attempts to create data before a specified invalid time
     Given there is a field foo
       But the profile is invalid as foo can't be before 2018-10-01T50:00:00.000
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: User attempts to create data before a specified date and an invalidly formatted date
     Given there is a field foo
     But the profile is invalid as foo can't be before "2018-10-01"
       Then I am presented with an error message
     And no data is created

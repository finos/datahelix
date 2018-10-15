Feature: User can specify that a temporal date is lower than, but not equal to, a specified threshold

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than a specified date
     Given there is a field foo
       And foo is before 2018-10-10T00:00:00.000
       And foo is after 2018-10-01T00:00:00.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | 2018-10-01 |
       | 2018-10-02 |
       | 2018-10-03 |
       | 2018-10-04 |
       | 2018-10-05 |
       | 2018-10-06 |
       | 2018-10-07 |
       | 2018-10-08 |
       | 2018-10-09 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a month boundary that are less than a specified date
     Given there is a field foo
       And foo is before 2018-10-10T00:00:00.000
       And foo is after 2018-09-28T00:00:00.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | 2018-09-28 |
       | 2018-09-29 |
       | 2018-09-30 |
       | 2018-10-01 |
       | 2018-10-02 |
       | 2018-10-03 |
       | 2018-10-04 |
       | 2018-10-05 |
       | 2018-10-06 |
       | 2018-10-07 |
       | 2018-10-08 |
       | 2018-10-09 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a year boundary that are less than a specified date
     Given there is a field foo
       And foo is before 2018-01-03T00:00:00.000
       And foo is after 2017-12-25T00:00:00.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | 2017-12-25 |
       | 2017-12-26 |
       | 2017-12-27 |
       | 2017-12-28 |
       | 2017-12-29 |
       | 2017-12-30 |
       | 2017-12-31 |
       | 2018-01-01 |
       | 2018-01-02 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a leap year February boundary that are less than a specified date
     Given there is a field foo
       And foo is before 2016-03-03T00:00:00.000
       And foo is after 2016-02-25T00:00:00.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | 2016-02-25 |
       | 2016-02-26 |
       | 2016-02-27 |
       | 2016-02-28 |
       | 2016-02-29 |
       | 2016-03-01 |
       | 2016-03-02 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a non-leap year February boundary that are less than a specified date
     Given there is a field foo
       And foo is before 2017-03-03T00:00:00.000
       And foo is after 2017-02-25T00:00:00.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | 2017-02-25 |
       | 2017-02-26 |
       | 2017-02-27 |
       | 2017-02-28 |
       | 2017-03-01 |
       | 2017-03-02 |

Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values within a given minute that are less than a specified date and time
     Given there is a field foo
       And foo is before 2018-10-01T12:00:10.000
       And foo is after 2018-10-01T12:00:00.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tT"
     Then the following data should be generated:
       | foo      |
       | 12:00:00 |
       | 12:00:01 |
       | 12:00:02 |
       | 12:00:03 |
       | 12:00:04 |
       | 12:00:05 |
       | 12:00:06 |
       | 12:00:07 |
       | 12:00:08 |
       | 12:00:09 |

Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values across a minute boundary that are less than a specified date and time
     Given there is a field foo
       And foo is before 2018-10-01T12:01:05.000
       And foo is after 2018-10-01T12:00:57.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tT"
     Then the following data should be generated:
       | foo      |
       | 12:00:57 |
       | 12:00:58 |
       | 12:00:59 |
       | 12:01:00 |
       | 12:01:01 |
       | 12:01:02 |
       | 12:01:03 |
       | 12:01:04 |

Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values across an hour boundary that are less than a specified date and time
     Given there is a field foo
       And foo is before 2018-10-01T13:00:05.000
       And foo is after 2018-10-01T12:59:57.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tT"
     Then the following data should be generated:
       | foo      |
       | 12:59:57 |
       | 12:59:58 |
       | 12:59:59 |
       | 13:00:00 |
       | 13:00:01 |
       | 13:00:02 |
       | 13:00:03 |
       | 13:00:04 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than a specified date and a second specified date
     Given there is a field foo
       And foo is before 2018-10-10T00:00:00.000
       And foo is before 2018-10-09T00:00:00.000
       And foo is after 2018-10-01T00:00:00.000
       And foo is not null
       And foo is of type "temporal"
       And foo is formatted as "%tF"
     Then the following data should be generated:
       | foo        |
       | 2018-10-01 |
       | 2018-10-02 |
       | 2018-10-03 |
       | 2018-10-04 |
       | 2018-10-05 |
       | 2018-10-06 |
       | 2018-10-07 |
       | 2018-10-08 |
Feature: User can specify that a temporal date is lower than, or the same as, a specified threshold

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than or the same as a specified date
     Given there is a field foo
       And foo is before or at 2018-10-10
       And foo is after 2018-10-01
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
       | 2018-10-10 |


Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a month boundary that are less than of the same as a specified date
     Given there is a field foo
       And foo is before or at 2018-10-10
       And foo is after 2018-09-28
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
       | 2018-10-10 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a year boundary that are less than or the same as a specified date
     Given there is a field foo
       And foo is before or at 2018-01-03
       And foo is after 2017-12-25
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
       | 2018-01-03 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a leap year February boundary that are less than or the same as a specified date
     Given there is a field foo
       And foo is before or at 2016-03-03
       And foo is after 2016-02-25
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
       | 2016-03-03 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values across a non-leap year February boundary that are less than or equal to a specified date
     Given there is a field foo
       And foo is before or at 2017-03-03
       And foo is after 2017-02-25
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
       | 2017-03-03 |

#Scenario: User requires to create a temporal field with date (YYYY-MM) values within a given year that are less than or equal to a specified date
#     Given there is a field foo
#       And foo is before or at 2018-12
#       And foo is after 2018-01
#       And foo is not null
#       And foo is of type "temporal"
#       And foo is formatted as "%tF"
#     Then the following data should be generated:
#       | foo     |
#       | 2018-02 |
#       | 2018-03 |
#       | 2018-04 |
#       | 2018-05 |
#       | 2018-06 |
#       | 2018-07 |
#       | 2018-08 |
#       | 2018-09 |
#       | 2018-10 |
#       | 2018-11 |
#       | 2018-12 |

#Scenario: User requires to create a temporal field with date (YYYY) values within a given decade that are less than or the same as a specified date
#     Given there is a field foo
#       And foo is before or at 2010
#       And foo is after 2000
#       And foo is not null
#       And foo is of type "temporal"
#       And foo is formatted as "%tF"
#     Then the following data should be generated:
#       | foo  |
#       | 2001 |
#       | 2002 |
#       | 2003 |
#       | 2004 |
#       | 2005 |
#       | 2006 |
#       | 2007 |
#       | 2008 |
#       | 2009 |
#       | 2010 |

Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values within a given minute that are less than or the same as a specified date and time
     Given there is a field foo
       And foo is before or at 2018-10-01T12:00:10
       And foo is after 2018-10-01T12:00:00
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
       | 12:00:10 |

Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values across a minute boundary that are less than or the same as a specified date and time
     Given there is a field foo
       And foo is before or at 2018-10-01T12:01:05
       And foo is after 2018-10-01T12:00:57
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
       | 12:01:05 |

Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM:SS) values across an hour boundary that are less than or the same as a specified date and time
     Given there is a field foo
       And foo is before or at 2018-10-01T13:00:05
       And foo is after 2018-10-01T12:59:57
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
       | 13:00:05 |

#Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH:MM) values within a given hour that are less than or the same as a specified time
#     Given there is a field foo
#       And foo is before or at 2018-10-01T12:00
#       And foo is after 2018-10-01T11:50
#       And foo is not null
#       And foo is of type "temporal"
#       And foo is formatted as "%tF"
#     Then the following data should be generated:
#       | foo              |
#       | 2018-10-01T11:51 |
#       | 2018-10-01T11:52 |
#       | 2018-10-01T11:53 |
#       | 2018-10-01T11:54 |
#       | 2018-10-01T11:55 |
#       | 2018-10-01T11:56 |
#       | 2018-10-01T11:57 |
#       | 2018-10-01T11:58 |
#       | 2018-10-01T11:59 |
#       | 2018-10-01T12:00 |

#Scenario: User requires to create a temporal field with date and time (YYYY-MM-DDTHH) values within a given day that are less than or the same as a specified time
#     Given there is a field foo
#       And foo is before or at 2018-10-01T12
#       And foo is after 2018-10-01T01
#       And foo is not null
#       And foo is of type "temporal"
#       And foo is formatted as "%tF"
#     Then the following data should be generated:
#       | foo           |
#       | 2018-10-01T02 |
#       | 2018-10-01T03 |
#       | 2018-10-01T04 |
#       | 2018-10-01T05 |
#       | 2018-10-01T06 |
#       | 2018-10-01T07 |
#       | 2018-10-01T08 |
#       | 2018-10-01T09 |
#       | 2018-10-01T10 |
#       | 2018-10-01T11 |
#       | 2018-10-01T12 |

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than a specified date and a less than or equal to a second specified date
     Given there is a field foo
       And foo is before 2018-10-10
       And foo is before or at 2018-10-09
       And foo is after 2018-10-01
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

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than or the same as a specified date and a less than a second specified date
     Given there is a field foo
       And foo is before or at 2018-10-10
       And foo is before 2018-10-09
       And foo is after 2018-10-01
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

Scenario: User requires to create a temporal field with date (YYYY-MM-DD) values within a given month that are less than or the same as a specified date and a less than or the same as a second specified date
     Given there is a field foo
       And foo is before or at 2018-10-10
       And foo is before or at 2018-10-09
       And foo is after 2018-10-01
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
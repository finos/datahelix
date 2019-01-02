Feature: User can generate interesting values whilst specifying that a number is higher than, but not equal to, a specified threshold

Background:
     Given the generation strategy is interesting

@ignore
Scenario: User creates data higher than a specified number
     Given there is a field foo
       And foo is greater than 0
     Then the following data should be included in what is generated:
       | foo     |
       | 1       |
       | 2       |
       | 3       |
       | 4       |
       | 5       |
       | 6       |
       | 7       |
       | 8       |
       | 9       |
       | 11      |
       | 101     |
       | 1001    |
       | 1000001 |
       | "01"    |
       | "001"   |
       | "0001"  |
       | "00001" |
       | "+1"    |
     And the following data should not be included in what is generated:
       | foo      |
       | 0        |
       | -1       |
       | -2       |
       | -3       |
       | -4       |
       | -5       |
       | -6       |
       | -7       |
       | -8       |
       | -9       |
       | -10      |
       | -100     |
       | -1000    |
       | -1000000 |
       | "-01"    |
       | "-001"   |
       | "-0001"  |
       | "-00001" |

@ignore
Scenario: User creates data higher than a specified negative number
     Given there is a field foo
       And foo is greater than -10
     Then the following data should be included in what is generated:
       | foo     |
       | -1      |
       | -2      |
       | -3      |
       | -4      |
       | -5      |
       | -6      |
       | -7      |
       | -8      |
       | -9      |
       | 10      |
       | 100     |
       | 1000    |
       | 1000000 |
       | "01"    |
       | "001"   |
       | "0001"  |
       | "00001" |
       | "+1"    |
     And the following data should not be included in what is generated:
       | foo       |
       | -10       |
       | -11       |
       | -12       |
       | -13       |
       | -14       |
       | -15       |
       | -16       |
       | -17       |
       | -18       |
       | -19       |
       | -110      |
       | -1010     |
       | -1000010  |
       | "-010"    |
       | "-0010"   |
       | "-00010"  |
       | "-000010" |

@ignore
Scenario: User creates data higher than a specified decimal number that would round down
     Given there is a field foo
       And foo is greater than 0.1
     Then the following data should be included in what is generated:
       | foo     |
       | 1       |
       | 2       |
       | 3       |
       | 4       |
       | 5       |
       | 6       |
       | 7       |
       | 8       |
       | 9       |
       | 11      |
       | 101     |
       | 1001    |
       | 1000001 |
       | "01"    |
       | "001"   |
       | "0001"  |
       | "00001" |
       | "+1"    |
     And the following data should not be included in what is generated:
       | foo      |
       | 0        |
       | -1       |
       | -2       |
       | -3       |
       | -4       |
       | -5       |
       | -6       |
       | -7       |
       | -8       |
       | -9       |
       | -11      |
       | -101     |
       | -1001    |
       | -1000001 |
       | "-01"    |
       | "-001"   |
       | "-0001"  |
       | "-00001" |

@ignore
Scenario: User creates data higher than a specified decimal number that would round up
     Given there is a field foo
       And foo is greater than 0.9
     Then the following data should be included in what is generated:
       | foo     |
       | 1       |
       | 2       |
       | 3       |
       | 4       |
       | 5       |
       | 6       |
       | 7       |
       | 8       |
       | 9       |
       | 11      |
       | 101     |
       | 1001    |
       | 1000001 |
       | "01"    |
       | "001"   |
       | "0001"  |
       | "00001" |
       | "+1"    |
     And the following data should not be included in what is generated:
       | foo      |
       | 0        |
       | -1       |
       | -2       |
       | -3       |
       | -4       |
       | -5       |
       | -6       |
       | -7       |
       | -8       |
       | -9       |
       | -11      |
       | -101     |
       | -1001    |
       | -1000001 |
       | "-01"    |
       | "-001"   |
       | "-0001"  |
       | "-00001" |

@ignore
Scenario: User creates data higher than two specified numbers
     Given there is a field foo
       And foo is greater than 0
       And foo is greater than 1
     Then the following data should be included in what is generated:
       | foo     |
       | 2       |
       | 3       |
       | 4       |
       | 5       |
       | 6       |
       | 7       |
       | 8       |
       | 9       |
       | 12      |
       | 102     |
       | 1002    |
       | 1000002 |
       | "02"    |
       | "002"   |
       | "0002"  |
       | "00002" |
       | "+2"    |
     And the following data should not be included in what is generated:
       | foo      |
       | 1        |
       | 0        |
       | -1       |
       | -2       |
       | -3       |
       | -4       |
       | -5       |
       | -6       |
       | -7       |
       | -8       |
       | -9       |
       | -11      |
       | -101     |
       | -1001    |
       | -1000001 |
       | "01"     |
       | "001"    |
       | "0001"   |
       | "00001"  |

@ignore
Scenario: User creates data that is anything but higher than a specified number
     Given there is a field foo
       And foo is anything but greater than 0
     Then the following data should be included in what is generated:
       | foo      |
       | 0        |
       | -1       |
       | -2       |
       | -3       |
       | -4       |
       | -5       |
       | -6       |
       | -7       |
       | -8       |
       | -9       |
       | -10      |
       | -100     |
       | -1000    |
       | -1000000 |
       | "-01"    |
       | "-001"   |
       | "-0001"  |
       | "-00001" |
     And the following data should not be included in what is generated:
       | foo     |
       | 1       |
       | 2       |
       | 3       |
       | 4       |
       | 5       |
       | 6       |
       | 7       |
       | 8       |
       | 9       |
       | 11      |
       | 101     |
       | 1001    |
       | 1000001 |
       | "01"    |
       | "001"   |
       | "0001"  |
       | "00001" |
       | "+1"    |

@ignore
Scenario: User attempts to data higher than a specified string
     Given there is a field foo
       And foo is greater than "$10.00"
     Then the profile is invalid
       And no data is created

@ignore
Scenario: User attempts to data higher than a specified number formatted as a string
     Given there is a field foo
       And foo is greater than "1"
     Then the profile is invalid
       And no data is created

@ignore
Scenario: User attempts to data higher than a specified date
     Given there is a field foo
       And foo is greater than 2018-10-10T00:00:00.000
     Then the profile is invalid
       And no data is created
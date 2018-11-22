Feature: User can specify that a field is of a specific type (string, numeric or temporal).

Background:
     Given the generation strategy is full

Scenario: Running an 'ofType' = string request that includes strings with roman alphabet lowercase chars (a-z) only should be successful
     Given there is a field foo
       And foo is equal to "abcdefghijklmnopqrstuvwxyz"
       And foo is of type "string"
     Then the following data should be generated:
       | foo                          |
       | "abcdefghijklmnopqrstuvwxyz" |

Scenario: Running an 'ofType' = String request that includes strings with roman alphabet uppercase chars (A-Z) only should be successful
     Given there is a field foo
       And foo is equal to "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
       And foo is of type "string"
     Then the following data should be generated:
       | foo                          |
       | "ABCDEFGHIJKLMNOPQRSTUVWXYZ" |

Scenario: Running an 'ofType' = String request that includes strings with roman numeric chars (0-9) only should be successful
     Given there is a field foo
       And foo is equal to "0123456789"
       And foo is of type "string"
     Then the following data should be generated:
       | foo          |
       | "0123456789" |

Scenario: Running an 'ofType' = String request that includes strings with both roman alphabet lowercase (a-z) and uppercase (A-Z) should be successful
     Given there is a field foo
       And foo is equal to "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
       And foo is of type "string"
     Then the following data should be generated:
       | foo                                                    |
       | "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" |

Scenario: Running an 'ofType' = String request that includes strings with both roman alphabet (a-z, A-Z)and numeric chars (0-9) should be successful
     Given there is a field foo
       And foo is equal to "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
       And foo is of type "string"
     Then the following data should be generated:
       | foo                                                              |
       | "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" |

Scenario: Running an 'ofType' = String request that includes roman character strings that include profanity should be successful
     Given there is a field foo
       And foo is equal to "Dick Van Dyke"
       And foo is of type "string"
     Then the following data should be generated:
       | foo             |
       | "Dick Van Dyke" |

Scenario: Running an 'ofType' = String request that includes roman character strings that include in-use values should be successful
     Given there is a field foo
       And foo is in set:
       | "true"      |
       | "false"     |
       | "null"      |
       | "undefined" |
       And foo is of type "string"
     Then the following data should be generated:
       | foo         |
       | "true"      |
       | "false"     |
       | "null"      |
       | "undefined" |

Scenario: Running an 'ofType' = String request that includes strings with special characters should be successful
     Given there is a field foo
       And foo is in set:
       | "!?:;()&%+-="                                |
       | "]	[] [] [] [] [] [] ["                       |
       | "†ŠŒŽ™¼ǅ©®…¶Σ֎"                               |
       | "☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |
       | "传/傳象形字ФХѰѾЦИتشرقصف"                      |
       And foo is of type "string"
     Then the following data should be generated:
       | foo                                            |
       | "!?:;()&%+-="                                |
       | "]	[] [] [] [] [] [] ["                       |
       | "†ŠŒŽ™¼ǅ©®…¶Σ֎"                               |
       | "☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |
       | "传/傳象形字ФХѰѾЦИتشرقصف"                      |

Scenario: Running an 'ofType' = String request that includes strings with special characters (standard) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set:
       | "abcdefghijklmnop  !?:;()&%+-="                                  |
       | "abcdefghijklmnop  ]	[] [] [] [] [] [] ["                       |
       | "abcdefghijklmnop  †ŠŒŽ™¼ǅ©®…¶Σ֎"                               |
       | "abcdefghijklmnop  ☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |
       | "abcdefghijklmnop  传/傳象形字ФХѰѾЦИتشرقصف"                      |
       And foo is of type "string"
     Then the following data should be generated:
       | foo                                                              |
       | "abcdefghijklmnop  !?:;()&%+-="                                  |
       | "abcdefghijklmnop  ]	[] [] [] [] [] [] ["                       |
       | "abcdefghijklmnop  †ŠŒŽ™¼ǅ©®…¶Σ֎"                               |
       | "abcdefghijklmnop  ☺☹☻😀😁😂😃😄😅😆😇😈😉😊😋😌🚩🚪🚫🚬🚭🚮🚯🚰" |
       | "abcdefghijklmnop  传/傳象形字ФХѰѾЦИتشرقصف"                      |

Scenario: Running an 'ofType' = String request that includes roman numeric strings that include decimal numbers should be successful
     Given there is a field foo
       And foo is in set:
       | "0.0"          |
       | "0.0.1"        |
       | "99.999999000" |
       And foo is of type "string"
     Then the following data should be generated:
       | foo            |
       | "0.0"          |
       | "0.0.1"        |
       | "99.999999000" |

Scenario: Running an 'ofType' = String request that includes roman numeric strings that include comma separated numbers should be successful
     Given there is a field foo
       And foo is in set:
       | "1,000"   |
       | "100,000" |
       | "5,99"    |
       And foo is of type "string"
     Then the following data should be generated:
       | foo       |
       | "1,000"   |
       | "100,000" |
       | "5,99"    |

Scenario: Running an 'ofType' = String request that includes roman numeric strings that include numbers with Preceding zeros ("010") should be successful
     Given there is a field foo
       And foo is in set:
       | "010"       |
       | "0001 0100" |
       And foo is of type "string"
     Then the following data should be generated:
       | foo         |
       | "010"       |
       | "0001 0100" |

Scenario: Running an 'ofType' = String request that includes roman numeric strings that include numbers with Preceding and trailing zeros should be successful
     Given there is a field foo
       And foo is in set:
       | "000010"     |
       | "10000"      |
       | "0000.00000" |
       And foo is of type "string"
     Then the following data should be generated:
       | foo          |
       | "000010"     |
       | "10000"      |
       | "0000.00000" |

Scenario: Running an 'ofType' = String request that includes roman numeric strings that include numbers in a currency style should be successful
     Given there is a field foo
       And foo is in set:
       | "£1.00" |
       | "€5,99" |
       And foo is of type "string"
     Then the following data should be generated:
       | foo     |
       | "£1.00" |
       | "€5,99" |

Scenario: Running an 'ofType' = String request that includes roman numeric strings that include positive and negative signed numbers should be successful
     Given there is a field foo
  And foo is in set:
    | "+5"      |
    | "-99"     |
    | "-500.05" |
  And foo is of type "string"
  Then the following data should be generated:
    | foo       |
    | "+5"      |
    | "-99"     |
    | "-500.05" |

Scenario: Running an 'ofType' = String request that includes roman character strings that include in-use numeric values should be successful
     Given there is a field foo
       And foo is in set:
       | "Infinity" |
       | "NaN"      |
       | "nil"      |
       And foo is of type "string"
     Then the following data should be generated:
       | foo        |
       | "Infinity" |
       | "NaN"      |
       | "nil"      |

Scenario: Running an 'ofType' = String request that includes roman character strings that include computer formatted numbers ("1E+02", "001 000") should be successful
     Given there is a field foo
       And foo is in set:
       | "1E+02"    |
       | "001 001"  |
       And foo is of type "string"
     Then the following data should be generated:
       | foo        |
       | "1E+02"    |
       | "001 001"  |

Scenario: Running an 'ofType' = String request that includes roman character strings that include valid date values ("2010-01-01T00:00:00.000") should be successful
     Given there is a field foo
       And foo is in set:
       | "2010-01-01T00:00:00.000" |
       | "2010-02-29T23:59:59.000" |
       And foo is of type "string"
     Then the following data should be generated:
       | foo                       |
       | "2010-01-01T00:00:00.000" |
       | "2010-02-29T23:59:59.000" |

Scenario: Running an 'ofType' = String request that includes roman character strings that include invalid date values should be successful
     Given there is a field foo
       And foo is in set:
       | "2010-01-01T00:00"        |
       | "01-01-2010T00:00:00,000" |
       | "1st January 2010"        |
       And foo is of type "string"
     Then the following data should be generated:
       | foo                       |
       | "2010-01-01T00:00"        |
       | "01-01-2010T00:00:00,000" |
       | "1st January 2010"        |

Scenario: Running an 'ofType' = numeric request that includes a number value (not a string) should be successful
     Given there is a field foo
       And foo is equal to 1
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo |
       | 1   |

Scenario: Running an 'ofType' = numeric request that includes a decimal number value should be successful
     Given there is a field foo
       And foo is equal to 0.66
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo  |
       | 0.66 |

Scenario: Running an 'ofType' = numeric request that includes a negative number value should be successful
     Given there is a field foo
       And foo is equal to -99.4
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo   |
       | -99.4 |

Scenario: Running an 'ofType' = numeric request that includes the number zero should be successful
     Given there is a field foo
       And foo is equal to 0
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo   |
       | 0     |

Scenario: Running an 'ofType' = temporal request that includes a date value (not a string) should be successful
     Given there is a field foo
       And foo is equal to 2010-01-01T00:00:00.000
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo                     |
       | 2010-01-01T00:00:00.000 |

Scenario: Running an 'ofType' = temporal request that includes a date value (leap year) should be successful
     Given there is a field foo
       And foo is equal to 2020-02-29T09:15:00.000
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo                     |
       | 2020-02-29T09:15:00.000 |

Scenario: Running an 'ofType' = temporal request that includes a date value (system epoch dates) should be successful
     Given there is a field foo
       And foo is in set:
       | 0001-01-01T00:00:00.001 |
       | 1970-01-01T00:00:00.000 |
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo                     |
       | 0001-01-01T00:00:00.001 |
       | 1970-01-01T00:00:00.000 |

Scenario: Running an 'ofType' = temporal request that includes a date value (system max future dates) should be successful
     Given there is a field foo
        And foo is equal to 9999-12-31T23:59:59.999
        And foo is of type "temporal"
     Then the following data should be generated:
       | foo                      |
       | 9999-12-31T23:59:59.999  |

Scenario: Running an 'ofType' = temporal request that includes an invalid date value should fail with an error message
     Given there is a field foo
       And foo is equal to 2010-13-40T00:00:00.000
       And foo is of type "temporal"
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'ofType' = temporal request that includes an invalid time value should fail with an error message
     Given there is a field foo
       And foo is equal to 2010-01-01T75:00:00.000
       And foo is of type "temporal"
     Then I am presented with an error message
       And no data is created

Scenario: Running an 'ofType' = string request that includes a null entry ("") characters should be successful
     Given there is a field foo
       And foo is equal to ""
       And foo is of type "string"
     Then the following data should be generated:
       | foo |
       | ""  |

Scenario: Running an 'ofType' = string request that includes a null entry (null) characters should be successful
     Given there is a field foo
       And foo is null
       And foo is of type "string"
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running an 'ofType' = numeric request that includes a null entry ("") characters should be successful
     Given there is a field foo
       And foo is equal to ""
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo |
       | ""  |

Scenario: Running an 'ofType' = numeric request that includes a null entry (null) characters should be successful
     Given there is a field foo
       And foo is null
       And foo is of type "numeric"
     Then the following data should be generated:
       | foo  |
       | null |

Scenario: Running an 'ofType' = temporal request that includes a null entry ("") characters should be successful
     Given there is a field foo
       And foo is equal to ""
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo |
       | ""  |

Scenario: Running an 'ofType' = temporal request that includes a null entry (null) characters should be successful
     Given there is a field foo
       And foo is null
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo  |
       | null |

@ignore
Scenario: Running an 'ofType' = string request that includes a number value should fail with an error message
     Given there is a field foo
       And foo is equal to 2
       And foo is of type "string"
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: Running an 'ofType' = string request that includes a temporal value should fail with an error message
     Given there is a field foo
       And foo is equal to 2010-01-01T00:00:00.000
       And foo is of type "string"
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: Running an 'ofType' = numeric request that includes a string value should fail with an error message
     Given there is a field foo
       And foo is equal to "2"
       And foo is of type "numeric"
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: Running an 'ofType' = numeric request that includes a temporal value should fail with an error message
     Given there is a field foo
       And foo is equal to 2010-01-01T00:00:00.000
       And foo is of type "numeric"
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: Running an 'ofType' = temporal request that includes a string value should fail with an error message
     Given there is a field foo
       And foo is equal to "2010-01-01T00:00:00.000"
       And foo is of type "temporal"
     Then I am presented with an error message
       And no data is created

@ignore
Scenario: Running an 'ofType' = temporal request that includes a number value should fail with an error message
     Given there is a field foo
       And foo is equal to 2
       And foo is of type "temporal"
     Then I am presented with an error message
       And no data is created
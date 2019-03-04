Feature: User can specify that a field is of a specific type (string, numeric or temporal).

Background:
     Given the generation strategy is full

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
       | null        |
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
       | null                                           |
       | "!?:;()&%+-="                                  |
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
       | null                                                             |
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
       | null           |
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
       | null      |
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
       | null        |
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
       | null         |
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
       | null    |
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
    | null      |
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
       | null       |
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
       | null       |
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
       | null                      |
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
       | null                      |
       | "2010-01-01T00:00"        |
       | "01-01-2010T00:00:00,000" |
       | "1st January 2010"        |

Scenario: Running an 'ofType' = temporal request that includes a date value (system epoch dates) should be successful
     Given there is a field foo
       And foo is in set:
       | 0001-01-01T00:00:00.001 |
       | 1970-01-01T00:00:00.000 |
       And foo is of type "temporal"
     Then the following data should be generated:
       | foo                     |
       | null                    |
       | 0001-01-01T00:00:00.001 |
       | 1970-01-01T00:00:00.000 |

Feature: User can specify that a field value belongs to a set of predetermined options.

Background:
     Given the generation strategy is full

Scenario: Running an 'inSet' request that includes strings with roman alphabet lowercase chars (a-z) only should be successful
     Given there is a field foo
       And foo is in set [ "aaa", "aab" ]
     Then the following data should be generated:
       |  foo  |
       | "aaa" |
       | "aab" |

Scenario: Running an 'inSet' request that includes strings with roman alphabet uppercase chars (A-Z) only should be successful
     Given there is a field foo
       And foo is in set [ "CCC", "DDD" ]
     Then the following data should be generated:
       |  foo  |
       | "CCC" |
       | "DDD" |

Scenario: Running an 'inSet' request that includes strings with roman numeric chars (0-9) only should be successful
     Given there is a field foo
       And foo is in set [ "012", "345" ]
     Then the following data should be generated:
       |  foo  |
       | "012" |
       | "345" |

Scenario: Running an 'inSet' request that includes strings with both roman alphabet lowercase (a-z) and uppercase (A-Z) should be successful
     Given there is a field foo
       And foo is in set [ "aAbB", "AaBb" ]
     Then the following data should be generated:
       |  foo   |
       | "aAbB" |
       | "AaBb" |

Scenario: Running an 'inSet' request that includes strings with both roman alphabet (a-z, A-Z)and numeric chars (0-9) should be successful
     Given there is a field foo
       And foo is in set [ "Testing01", "Testing02" ]
     Then the following data should be generated:
       |  foo        |
       | "Testing01" |
       | "Testing02" |

Scenario: Running an 'inSet' request that includes roman character strings that include profanity should be successful
     Given there is a field foo
       And foo is in set [ "Dick Van Dyke", "Scunthorpe Hospital" ]
     Then the following data should be generated:
       |  foo                  |
       | "Dick Van Dyke"       |
       | "Scunthorpe Hospital" |


Scenario: Running an 'inSet' request that includes roman character strings that include in-use values should be successful
     Given there is a field foo
       And foo is in set [ "true", "false", "null", "undefined" ]
     Then the following data should be generated:
       |  foo        |
       | "true"      |
       | "false"     |
       | "null"      |
       | "undefined" |

Scenario: Running an 'inSet' request that includes strings with special characters (standard) should be successful
     Given there is a field foo
       And foo is in set [ "!£$%^&*()", "{}:@~;'#<>?" ]
     Then the following data should be generated:
       |  foo          |
       | "!£$%^&*()"   |
       | "{}:@~;'#<>?" |

Scenario: Running an 'inSet' request that includes strings with special characters (white spaces) should be successful
     Given there is a field foo
       And foo is in set [ "]	[] [] [] [", "] [] [] [" ]
     Then the following data should be generated:
       |  foo           |
       | "]	[] [] [] [" |
       | "] [] [] ["    |

Scenario: Running an 'inSet' request that includes strings with special characters (unicode symbols) should be successful
     Given there is a field foo
       And foo is in set [ "†ŠŒŽ™¼ǅ©", "®…¶Σ֎" ]
     Then the following data should be generated:
       |  foo       |
       | "†ŠŒŽ™¼ǅ©" |
       | "®…¶Σ֎"    |

Scenario: Running an 'inSet' request that includes strings with special characters (emoji) should be successful
     Given there is a field foo
       And foo is in set [ "🚫⌛⚡🐢", "👟💪😈🔬" ]
     Then the following data should be generated:
       |  foo       |
       | "🚫⌛⚡🐢"   |
       | "👟💪😈🔬" |

Scenario: Running an 'inSet' request that includes strings with special characters (non roman character maps) should be successful
     Given there is a field foo
       And foo is in set [ "Ω", "ڦ", "আ", "⾉", "㑹", "㾹"  ]
     Then the following data should be generated:
       | foo  |
       | "Ω"  |
       | "ڦ"  |
       | "আ" |
       | "⾉" |
       | "㑹" |
       | "㾹" |

Scenario: Running an 'inSet' request that includes strings with special characters (standard) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set [ "£1.00", "$5.00", "Testing (01)" ]
     Then the following data should be generated:
       | foo            |
       | "£1.00"        |
       | "$5.00"        |
       | "Testing (01)" |

Scenario: Running an 'inSet' request that includes strings with special characters (white spaces) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set [ "Test	One", "Test 02", "Test Three" ]
     Then the following data should be generated:
       | foo            |
       | "Test	One"    |
       | "Test 02"     |
       | "Test Three"    |

Scenario: Running an 'inSet' request that includes strings with special characters (unicode symbols) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set [ "I will display ♠", "★ I will display ★", "♞♟♜⚣ Displaying symbols" ]
     Then the following data should be generated:
       | foo                          |
       | "I will display ♠"           |
       | "★ I will display ★"        |
       | "♞♟♜⚣ Displaying symbols" |

Scenario: Running an 'inSet' request that includes strings with special characters (emoji) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set [ "Test 01 has passed 😃", "❤ Test 02 ❤", "Passing Tests ☑" ]
     Then the following data should be generated:
       | foo                     |
       | "Test 01 has passed 😃" |
       | "❤ Test 02 ❤"          |
       | "Passing Tests ☑"       |

Scenario: Running an 'inSet' request that includes strings with special characters (non roman character maps) alongside roman alphanumeric characters should be successful
     Given there is a field foo
       And foo is in set [ "Cyrillic text: Тхис ис Тест Нумбер 01", "Japanese text: これはテスト番号2です", "Korean text: 이것은 시험 번호 3입니다" ]
     Then the following data should be generated:
       | foo                                     |
       | "Cyrillic text: Тхис ис Тест Нумбер 01" |
       | "Japanese text: これはテスト番号2です"     |
       | "Korean text: 이것은 시험 번호 3입니다"    |

Scenario: Running an 'inSet' request that includes roman numeric strings that include decimal numbers should be successful
     Given there is a field foo
       And foo is in set [ "0.1", "0.00", "12.5.99", "0000000.345" ]
     Then the following data should be generated:
       | foo           |
       | "0.1"         |
       | "0.00"        |
       | "12.5.99"     |
       | "0000000.345" |

Scenario: Running an 'inSet' request that includes roman numeric strings that include comma separated numbers should be successful
     Given there is a field foo
       And foo is in set [ "55,5", "10,000", "1,000,000.00" ]
     Then the following data should be generated:
       | foo            |
       | "55,5"         |
       | "10,000"       |
       | "1,000,000.00" |

Scenario: Running an 'inSet' request that includes roman numeric strings that include numbers with Preceding zeros should be successful
     Given there is a field foo
       And foo is in set [ "001", "010", "01.00", "000000000.0000000001" ]
     Then the following data should be generated:
       | foo                    |
       | "001"                  |
       | "010"                  |
       | "01.00"                |
       | "000000000.0000000001" |


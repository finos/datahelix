Feature: User can specify that a value is so formatted

  Background:
    Given the generation strategy is full
    And there is a field foo

  Scenario Outline: Running a valid 'formattedAs' request on numbers should be successful
    Given foo is in set:
      | <input> |
    And foo has type "decimal"
    And foo has formatting <format>
    And foo is anything but null
    Then the following data should be generated:
      | foo        |
      | <expected> |
    Examples:
      | input                    | format      | expected                     |
#      | 1.0                      | "%a"         | "0x1.0p0"                      | no way to specify float over double
#      | 1.5                      | "%a"         | "0x1.8p0"                      | no way to specify float over double
      | 1                        | "%b"        | "true"                           |
      | 32                       | "%c"        | " "                         |
      | 0                        | "%d"        | "0"                          |
      | 1                        | "%d"        | "1"                          |
      | 1                        | "%20d"      | "                   1"       |
      | 1                        | "%-20d"     | "1                   "       |
      | 1                        | "%020d"     | "00000000000000000001"       |
      | 1                        | "\|%+20d\|" | "\|                  +1\|"   |
      | -1                       | "\|%+20d\|" | "\|                  -1\|"   |
      | 1                        | "% d"       | " 1"                         |
      | 1                        | "%,d"       | "1"                          |
      | 1111111111111111111      | "%,d"       | "1,111,111,111,111,111,111"  |
      | 1                        | "%(d"       | "1"                          |
      | -1                       | "%(d"       | "(1)"                        |
      | 1.0                      | "%e"        | "1.000000e+00"               |
      | 123456789.123456789      | "%e"        | "1.234568e+08"               |
      | 1.0                      | "%f"        | "1.000000"                   |
      | -1.0                     | "%f"        | "-1.000000"                  |
      | 123456789.123456789      | "%f"        | "123456789.123457"           |
      | 1.0                      | "%g"        | "1.00000"                    |
      | 123456789.123456789      | "%g"        | "1.23457e+08"                |
      | 0                        | "%o"        | "0"                          |
      | 123456789                | "%o"        | "726746425"                  |
      | -123456789               | "%o"        | "37051031353"                |
      | 1                        | "%s"        | "1"                          |
      | 1                        | "%10s"      | "         1"                 |
      | 1                        | "%-10s"     | "1         "                 |
      | 0                        | "%x"        | "0"                          |
      | 1                        | "%x"        | "1"                          |
      | 123456789                | "%x"        | "75bcd15"                    |
      | -123456789               | "%x"        | "f8a432eb"                   |
      | 11111111111              | "%-10s"     | "11111111111"                |
      | 1                        | "%.5s"      | "1"                          |
      | 11111111111              | "%.5s"      | "11111"                      |
      | 12345678901              | "%10.5s"    | "     12345"                 |



  Scenario Outline: Running a valid 'formattedAs' request on strings should be successful
    Given foo is in set:
      | <input> |
    And foo has type "string"
    And foo has formatting <format>
    And foo is anything but null
    Then the following data should be generated:
      | foo        |
      | <expected> |
    Examples:
      | input                    | format      | expected                     |
      | "1"                      | "%s"        | "1"                          |
      | ""                       | "%10s"      | "          "                 |
      | "1"                      | "%-10s"     | "1         "                 |
      | "11111111111"            | "%.5s"      | "11111"                      |
      | "12345678901"            | "%10.5s"    | "     12345"                 |


  Scenario Outline: Running a valid 'formattedAs' request on datetime should be successful
    Given foo is in set:
      | <input> |
    And foo has type "datetime"
    And foo has formatting <format>
    And foo is anything but null
    Then the following data should be generated:
      | foo        |
      | <expected> |
    Examples:
      | input                    | format      | expected                     |
      | 2018-10-10T00:00:00.000Z | "%s"        | "2018-10-10T00:00Z"          |
      | 2018-10-10T00:00:10.000Z | "%s"        | "2018-10-10T00:00:10Z"       |
      | 2018-10-10T00:00:01.000Z | "%s"        | "2018-10-10T00:00:01Z"       |
      | 2018-10-10T00:00:59.100Z | "%s"        | "2018-10-10T00:00:59.100Z"   |
      | 2018-10-10T00:00:59.001Z | "%s"        | "2018-10-10T00:00:59.001Z"   |
      | 2018-10-10T00:00:00.000Z | "%10s"      | "2018-10-10T00:00Z"          |
      | 2018-10-10T00:00:10.000Z | "%10s"      | "2018-10-10T00:00:10Z"       |
      | 2018-10-10T00:00:01.000Z | "%10s"      | "2018-10-10T00:00:01Z"       |
      | 2018-10-10T00:00:59.100Z | "%10s"      | "2018-10-10T00:00:59.100Z"   |
      | 2018-10-10T00:00:59.001Z | "%10s"      | "2018-10-10T00:00:59.001Z"   |
      | 2018-10-10T00:00:00.000Z | "%-10s"     | "2018-10-10T00:00Z"          |
      | 2018-10-10T00:00:10.000Z | "%-10s"     | "2018-10-10T00:00:10Z"       |
      | 2018-10-10T00:00:01.000Z | "%-10s"     | "2018-10-10T00:00:01Z"       |
      | 2018-10-10T00:00:59.100Z | "%-10s"     | "2018-10-10T00:00:59.100Z"   |
      | 2018-10-10T00:00:59.001Z | "%-10s"     | "2018-10-10T00:00:59.001Z"   |
      | 2018-10-10T00:00:00.000Z | "%.5s"      | "2018-"                      |
      | 2018-10-10T00:00:10.000Z | "%.5s"      | "2018-"                      |
      | 2018-10-10T00:00:01.000Z | "%.5s"      | "2018-"                      |
      | 2018-10-10T00:00:59.100Z | "%.5s"      | "2018-"                      |
      | 2018-10-10T00:00:59.001Z | "%.5s"      | "2018-"                      |
      | 2018-10-10T00:00:00.000Z | "%10.5s"    | "     2018-"                 |
      | 2018-10-10T00:00:10.000Z | "%10.5s"    | "     2018-"                 |
      | 2018-10-10T00:00:01.000Z | "%10.5s"    | "     2018-"                 |
      | 2018-10-10T00:00:59.100Z | "%10.5s"    | "     2018-"                 |
      | 2018-10-10T00:00:59.001Z | "%10.5s"    | "     2018-"                 |
      | 2018-12-01T16:17:18.199Z | "%tA"       | "Saturday"                   |
      | 2018-12-01T16:17:18.199Z | "%ta"       | "Sat"                        |
      | 2018-12-01T16:17:18.199Z | "%tB"       | "December"                   |
      | 2018-12-01T16:17:18.199Z | "%tb"       | "Dec"                        |
      | 2018-12-01T16:17:18.199Z | "%tC"       | "20"                         |
#      | 2018-12-01T16:17:18.199Z | "%tc"        | "Sat Dec 11 16:17:18 UTC 2018" | requires timezone information
      | 2018-12-01T16:17:18.199Z | "%tD"       | "12/01/18"                   |
      | 2018-12-01T16:17:18.199Z | "%td"       | "01"                         |
      | 2018-12-01T16:17:18.199Z | "%te"       | "1"                          |
      | 2018-12-01T16:17:18.199Z | "%tF"       | "2018-12-01"                 |
      | 2018-12-01T16:17:18.199Z | "%tH"       | "16"                         |
      | 2018-12-01T16:17:18.199Z | "%th"       | "Dec"                        |
      | 2018-12-01T16:17:18.199Z | "%tI"       | "04"                         |
      | 2018-12-01T16:17:18.199Z | "%tj"       | "335"                        |
      | 2018-12-01T09:17:18.199Z | "%tk"       | "9"                          |
      | 2018-12-01T16:17:18.199Z | "%tl"       | "4"                          |
      | 2018-12-01T16:07:18.199Z | "%tM"       | "07"                         |
      | 2018-02-01T16:17:18.199Z | "%tm"       | "02"                         |
      | 2018-02-01T16:17:18.099Z | "%tN"       | "099000000"                  |
      | 2018-02-01T16:17:18.199Z | "%tp"       | "pm"                         |
#      | 2018-02-01T16:17:18.199Z  | "%tQ"        | "02"                           | requires timezone information
      | 2018-02-01T16:17:18.199Z | "%tR"       | "16:17"                      |
      | 2018-02-01T16:17:18.199Z | "%tr"       | "04:17:18 PM"                |
      | 2018-02-01T16:17:08.199Z | "%tS"       | "08"                         |
#      | 2018-02-01T16:17:18.199Z  | "%ts"        | "02"                           | requires timezone information
      | 2018-02-01T16:17:18.199Z | "%tT"       | "16:17:18"                   |
      | 2018-02-01T16:17:08.199Z | "%tY"       | "2018"                       |
      | 2018-02-01T16:17:08.199Z | "%ty"       | "18"                         |
#      | 2018-02-01T16:17:08.199Z  | "%tZ"        | "08"                           | requires timezone information
#      | 2018-02-01T16:17:08.199Z  | "%tz"        | "08"                           | requires timezone information

  @ignore #857: Format exceptions do not throw an InvalidProfile exception therefore cannot be caught and asserted against
  Scenario Outline: Running an invalid 'formattedAs' request should fail with an error message
    Given foo is in set:
      | <input> |
    And foo has formatting <format>
    And foo is anything but null
    Then the profile is invalid because "Unable to format value `.+` with format expression `.+`: .*"
    And no data is created
    Examples:
      | input                    | format      |
      | "1"                      | "%20d"      |
      | 2018-02-01T16:17:18.199Z | "%20d"      |
      | "1"                      | "%-20d"     |
      | 2018-02-01T16:17:18.199Z | "%-20d"     |
      | "1"                      | "%020d"     |
      | 2018-02-01T16:17:18.199Z | "%020d"     |
      | "1"                      | "\|%020d\|" |
      | 2018-02-01T16:17:18.199Z | "\|%020d\|" |
      | "1"                      | "% d"       |
      | 2018-02-01T16:17:18.199Z | "% d"       |
      | "1"                      | "%,d"       |
      | 2018-02-01T16:17:18.199Z | "%,d"       |
      | "1"                      | "%(d"       |
      | 2018-02-01T16:17:18.199Z | "%(d"       |
      | "1"                      | "%o"        |
      | 2018-02-01T16:17:18.199Z | "%o"        |
      | "1"                      | "%x"        |
      | 2018-02-01T16:17:18.199Z | "%x"        |
      | "1"                      | "%t"        |
      | 1                        | "%t"        |
      | "1"                      | "%tA"       |
      | 1                        | "%tA"       |
      | "1"                      | "%ta"       |
      | 1                        | "%ta"       |
      | "1"                      | "%tB"       |
      | 1                        | "%tB"       |
      | "1"                      | "%tb"       |
      | 1                        | "%tb"       |
      | "1"                      | "%tC"       |
      | 1                        | "%tC"       |
      | "1"                      | "%tc"       |
      | 1                        | "%tc"       |
      | "1"                      | "%tD"       |
      | 1                        | "%tD"       |
      | "1"                      | "%td"       |
      | 1                        | "%td"       |
      | "1"                      | "%te"       |
      | 1                        | "%te"       |
      | "1"                      | "%tF"       |
      | 1                        | "%tF"       |
      | "1"                      | "%tH"       |
      | 1                        | "%tH"       |
      | "1"                      | "%th"       |
      | 1                        | "%th"       |
      | "1"                      | "%tI"       |
      | 1                        | "%tI"       |
      | "1"                      | "%tj"       |
      | 1                        | "%tj"       |
      | "1"                      | "%tk"       |
      | 1                        | "%tk"       |
      | "1"                      | "%tl"       |
      | 1                        | "%tl"       |
      | "1"                      | "%tM"       |
      | 1                        | "%tM"       |
      | "1"                      | "%tm"       |
      | 1                        | "%tm"       |
      | "1"                      | "%tN"       |
      | 1                        | "%tN"       |
      | "1"                      | "%tp"       |
      | 1                        | "%tp"       |
      | "1"                      | "%tQ"       |
      | 1                        | "%tQ"       |
      | "1"                      | "%tR"       |
      | 1                        | "%tR"       |
      | "1"                      | "%tr"       |
      | 1                        | "%tr"       |
      | "1"                      | "%tS"       |
      | 1                        | "%tS"       |
      | "1"                      | "%ts"       |
      | 1                        | "%ts"       |
      | "1"                      | "%tT"       |
      | 1                        | "%tT"       |
      | "1"                      | "%tY"       |
      | 1                        | "%tY"       |
      | "1"                      | "%ty"       |
      | 1                        | "%ty"       |
      | "1"                      | "%tZ"       |
      | 1                        | "%tZ"       |
      | "1"                      | "%tz"       |
      | 1                        | "%tz"       |

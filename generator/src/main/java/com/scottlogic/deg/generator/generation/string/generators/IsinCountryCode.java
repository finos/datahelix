/*
 * Copyright 2019 Scott Logic Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.scottlogic.deg.generator.generation.string.generators;


import static com.scottlogic.deg.generator.generation.string.generators.ChecksumStringGeneratorFactory.createCusipGenerator;
import static com.scottlogic.deg.generator.generation.string.generators.ChecksumStringGeneratorFactory.createSedolGenerator;

public enum IsinCountryCode {
    AD,
    AE,
    AF,
    AG,
    AI,
    AL,
    AM,
    AO,
    AQ,
    AR,
    AS,
    AT,
    AU,
    AW,
    AX,
    AZ,
    BA,
    BB,
    BD,
    BE,
    BF,
    BG,
    BH,
    BI,
    BJ,
    BL,
    BM,
    BN,
    BO,
    BQ,
    BR,
    BS,
    BT,
    BV,
    BW,
    BY,
    BZ,
    CA,
    CC,
    CD,
    CF,
    CG,
    CH,
    CI,
    CK,
    CL,
    CM,
    CN,
    CO,
    CR,
    CU,
    CV,
    CW,
    CX,
    CY,
    CZ,
    DE,
    DJ,
    DK,
    DM,
    DO,
    DZ,
    EC,
    EE,
    EG,
    EH,
    ER,
    ES,
    ET,
    FI,
    FJ,
    FK,
    FM,
    FO,
    FR,
    GA,
    GB(prefix("GB00", createSedolGenerator())),
    GD,
    GE,
    GF,
    GG,
    GH,
    GI,
    GL,
    GM,
    GN,
    GP,
    GQ,
    GR,
    GS,
    GT,
    GU,
    GW,
    GY,
    HK,
    HM,
    HN,
    HR,
    HT,
    HU,
    ID,
    IE,
    IL,
    IM,
    IN,
    IO,
    IQ,
    IR,
    IS,
    IT,
    JE,
    JM,
    JO,
    JP,
    KE,
    KG,
    KH,
    KI,
    KM,
    KN,
    KP,
    KR,
    KW,
    KY,
    KZ,
    LA,
    LB,
    LC,
    LI,
    LK,
    LR,
    LS,
    LT,
    LU,
    LV,
    LY,
    MA,
    MC,
    MD,
    ME,
    MF,
    MG,
    MH,
    MK,
    ML,
    MM,
    MN,
    MO,
    MP,
    MQ,
    MR,
    MS,
    MT,
    MU,
    MV,
    MW,
    MX,
    MY,
    MZ,
    NA,
    NC,
    NE,
    NF,
    NG,
    NI,
    NL,
    NO,
    NP,
    NR,
    NU,
    NZ,
    OM,
    PA,
    PE,
    PF,
    PG,
    PH,
    PK,
    PL,
    PM,
    PN,
    PR,
    PS,
    PT,
    PW,
    PY,
    QA,
    RE,
    RO,
    RS,
    RU,
    RW,
    SA,
    SB,
    SC,
    SD,
    SE,
    SG,
    SH,
    SI,
    SJ,
    SK,
    SL,
    SM,
    SN,
    SO,
    SR,
    SS,
    ST,
    SV,
    SX,
    SY,
    SZ,
    TC,
    TD,
    TF,
    TG,
    TH,
    TJ,
    TK,
    TL,
    TM,
    TN,
    TO,
    TR,
    TT,
    TV,
    TW,
    TZ,
    UA,
    UG,
    UM,
    US(prefix("US", createCusipGenerator())),
    UY,
    UZ,
    VA,
    VC,
    VE,
    VG,
    VI,
    VN,
    VU,
    WF,
    WS,
    YE,
    YT,
    ZA,
    ZM,
    ZW;

    final static String GENERIC_NSIN_REGEX = "[A-Z0-9]{9}";
    private final StringGenerator checksumlessStringGenerator;

    IsinCountryCode(){
        checksumlessStringGenerator = prefix(this.name(),
            new RegexStringGenerator(GENERIC_NSIN_REGEX, true));
    }

    IsinCountryCode(StringGenerator stringGenerator){
        this.checksumlessStringGenerator = stringGenerator;
    }

    static StringGenerator prefix(String prefix, StringGenerator inner){
        return new PrefixingStringGenerator(prefix, inner);
    }

    public StringGenerator getChecksumlessStringGenerator() {
        return checksumlessStringGenerator;
    }
}

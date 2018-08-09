package com.scottlogic.deg.classifier.simple_classifier

import com.scottlogic.deg.classifier.{Classifier, CountryCodeType, SemanticType}

object CountryCodeClassifier extends Classifier {
  private val iso2CountryCodes : List[String] = List("AF","AX","AL","DZ","AS","AD","AO","AI","AQ","AG","AR","AM","AW","AU","AT","AZ","BS","BH","BD","BB","BY","BE","BZ","BJ","BM","BT","BO","BA","BW","BV","BR","VG","IO","BN","BG","BF","BI","KH","CM","CA","CV","KY","CF","TD","CL","CN","HK","MO","CX","CC","CO","KM","CG","CD","CK","CR","CI","HR","CU","CY","CZ","DK","DJ","DM","DO","EC","EG","SV","GQ","ER","EE","ET","FK","FO","FJ","FI","FR","GF","PF","TF","GA","GM","GE","DE","GH","GI","GR","GL","GD","GP","GU","GT","GG","GN","GW","GY","HT","HM","VA","HN","HU","IS","IN","ID","IR","IQ","IE","IM","IL","IT","JM","JP","JE","JO","KZ","KE","KI","KP","KR","KW","KG","LA","LV","LB","LS","LR","LY","LI","LT","LU","MK","MG","MW","MY","MV","ML","MT","MH","MQ","MR","MU","YT","MX","FM","MD","MC","MN","ME","MS","MA","MZ","MM","NA","NR","NP","NL","AN","NC","NZ","NI","NE","NG","NU","NF","MP","NO","OM","PK","PW","PS","PA","PG","PY","PE","PH","PN","PL","PT","PR","QA","RE","RO","RU","RW","BL","SH","KN","LC","MF","PM","VC","WS","SM","ST","SA","SN","RS","SC","SL","SG","SK","SI","SB","SO","ZA","GS","SS","ES","LK","SD","SR","SJ","SZ","SE","CH","SY","TW","TJ","TZ","TH","TL","TG","TK","TO","TT","TN","TR","TM","TC","TV","UG","UA","AE","GB","US","UM","UY","UZ","VU","VE","VN","VI","WF","EH","YE","ZM","ZW")
  override def classify(input: String): Seq[SemanticType] = {
    if(input == null || input == ""){
      return List[SemanticType]()
    }

    if(input.length != 2){
      return List[SemanticType]();
    }

    if(iso2CountryCodes.contains(input.toUpperCase)) {
      return Seq[SemanticType](CountryCodeType)
    }

    return List[SemanticType]()
  }
}
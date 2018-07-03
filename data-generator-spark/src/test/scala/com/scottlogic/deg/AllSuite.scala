package com.scottlogic.deg

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(classOf[Suite])
@Suite.SuiteClasses(Array(
  classOf[AppTest],
  classOf[FileReaderInt]
))
class AllSuite {

}

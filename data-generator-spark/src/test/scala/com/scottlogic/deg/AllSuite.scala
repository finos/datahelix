package com.scottlogic.deg

import org.junit.platform.runner.JUnitPlatform
import org.junit.platform.suite.api.{SelectClasses, SelectPackages}
import org.junit.runner.RunWith

@RunWith(classOf[JUnitPlatform])
@SelectClasses(Array(
  classOf[FileReaderInt],
  classOf[JsonTest],
  classOf[SemanticInferenceTest]
))
class AllSuite {

}

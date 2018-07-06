package com.scottlogic.deg

import org.junit.platform.runner.JUnitPlatform
import org.junit.platform.suite.api.SelectPackages
import org.junit.runner.RunWith

@RunWith(classOf[JUnitPlatform])
@SelectPackages(Array("com.scottlogic.deg"))
class AllSuite {

}

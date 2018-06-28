package com.scottlogic.deg

import java.io.FileNotFoundException

import org.junit._
import Assert._

@Test
class AppTest {
    @Test (expected = classOf[NullPointerException])
    def DEGApp_withoutArguments_throws_NullPointerException(): Unit = {
        val app = new com.scottlogic.deg.DEGApp()
        app.run(null)
    }
}

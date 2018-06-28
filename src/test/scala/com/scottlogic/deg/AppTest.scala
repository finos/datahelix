package com.scottlogic.deg

import java.io.FileNotFoundException

import org.junit._
import Assert._

@Test
class AppTest {
    @Test (expected = classOf[NullPointerException])
    def DEGApp_throws_NullPointerException_Without_Arguments(): Unit = {
        val app = new com.scottlogic.deg.DEGApp()
        app.run(null)
    }
}

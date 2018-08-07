package com.scottlogic.deg

import java.io.File

import com.scottlogic.deg.io.FileReader
import com.scottlogic.deg.profiler.Profiler
import javax.inject.Inject
import org.junit.jupiter.api.Assertions._
import org.junit.jupiter.api._

@Test
@RequiresInjection
class SemanticInferenceTest {
  @Inject
  var fileReader : FileReader = _

  @Test
  def inferTypesTest() {
    val path = getClass.getClassLoader.getResource("semantic.csv").getPath
    val df = fileReader.readCSV(new File(path))
    val profiler = new Profiler(df)
    val profile = profiler.profile()
    assertEquals(profile.Fields.size, 6)
  }
}

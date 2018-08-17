package com.scottlogic.deg

import java.io.File

import com.scottlogic.deg.classifier.SemanticTypeField
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
  def givenCSVFile_whenProfile_thenFieldsHaveBeenInferred() {
    val path = getClass.getClassLoader.getResource("semantic.csv").getPath
    val df = fileReader.readCSV(new File(path))
    val profile = Profiler.profile(df, List[SemanticTypeField]())
    assertEquals(profile.fields.size, 6)
  }
}

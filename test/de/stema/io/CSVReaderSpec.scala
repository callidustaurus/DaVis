package de.stema.io

import org.scalatestplus.play.PlaySpec

/**
  * Created by stefan on 29.04.17.
  */
class CSVReaderSpec extends PlaySpec {

  "read(...)" should {
    val expected = List(
      Map("Foo" -> "foo_1", "Baa" -> "baa_1", "Bar" -> "bar_1"),
      Map("Foo" -> "foo_2", "Baa" -> "baa_2", "Bar" -> "bar_2"),
      Map("Foo" -> "foo_3", "Baa" -> "baa_3", "Bar" -> "bar_3")
    )

    "be able to read simple csv-files" in {
      val data = CSVReader.read(path = "test/resources/csv_files/simple_csv.CSV")

      data mustBe expected
    }

    "be able to read cssv-files with specific separator" in {
      val SEPARATOR = ":"
      val data = CSVReader.read(path = "test/resources/csv_files/simple_csv_colon_separated.CSV", SEPARATOR)

      data mustBe expected
    }
  }
}

package de.stema.io

import org.scalatestplus.play.PlaySpec

class Testee {
  @CSVData("foo") val foo: String = null
  @CSVData("baa") val baa_val: String = null
  @CSVData("bar") val differentNameThen_Bar: String = null
}

class CSVDataMapperSpec extends PlaySpec {

  "transformDataTo(...)" should {
    val data = List(
      Map("foo" -> "a value for foo", "baa" -> "baa", "bar" -> "blubb"),
      Map("foo" -> "another value for foo", "baa" -> "baa", "bar" -> "blubb")
    )

    "map the given data to given model" in {
      val mapper = new CSVDataMapper()
      val result =mapper.transformDataTo[Testee](data)

      result.size mustBe 2

      result.head.foo mustBe "a value for foo"
      result.head.baa_val mustBe "baa"
      result.head.differentNameThen_Bar mustBe "blubb"

      result(1).foo mustBe "another value for foo"
      result(1).baa_val mustBe "baa"
      result(1).differentNameThen_Bar mustBe "blubb"
    }
  }
}

package de.stema.io

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import org.scalatest.mock.MockitoSugar
import org.scalatestplus.play.PlaySpec
import play.api.Configuration

class Testee extends Foo {
  @CSVData("foo") val foo: String = null
  @CSVData("baa") val baa_val: String = null
  @CSVData("bar") val differentNameThen_Bar: String = null
  @CSVData("myInt") val aNumber : BigInt = null
  @CSVData("myDouble") val aDecimalNumber : BigDecimal = null
  @CSVData("myBoolean") val trueOrNot : Boolean = false
  @CSVData("myDate") val dayMonthYear : LocalDate = null
}

trait Foo {
  @CSVData(Config.CONFIG_KEY) val withinTrait : String = null
}

class CSVDataMapperSpec extends PlaySpec {
  private val configuration = Configuration("foo" -> "baa")
  private val mapper = new CSVDataMapper(configuration)
  "transformDataTo(...)" should {
    val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val stringData = List(
      Map("foo" -> "a value for foo", "baa" -> "baa", "bar" -> "blubb"),
      Map("foo" -> "another value for foo", "baa" -> "baa", "bar" -> "blubb")
    )

    val mixedTypeData = List(
      Map("myInt" -> "1", "myDouble" -> "1.1", "myBoolean" -> "true", "myDate" -> "01.01.2000")
    )

    "map the given stringData to given model" in {

      val result = mapper.transformDataTo[Testee](stringData)

      result.size mustBe 2

      result.head.foo mustBe "a value for foo"
      result.head.baa_val mustBe "baa"
      result.head.differentNameThen_Bar mustBe "blubb"

      result(1).foo mustBe "another value for foo"
      result(1).baa_val mustBe "baa"
      result(1).differentNameThen_Bar mustBe "blubb"
    }

    "map the data according to field-type" in {
      val result = mapper.transformDataTo[Testee](mixedTypeData)

      result.size mustBe 1

      result.head.aNumber mustBe 1
      result.head.aDecimalNumber mustBe 1.1
      result.head.trueOrNot mustBe true
      result.head.dayMonthYear mustBe LocalDate.parse("01.01.2000", dateFormat)

    }

    "map date also to underlying traits" in {
      val data =    List(Map("foo" -> "foo value", "within a trait" -> "trait value"))
      val result = mapper.transformDataTo[Testee](data)

      result.size mustBe 1

      result.head.foo mustBe "foo value"
      result.head.withinTrait mustBe "trait value"
    }
  }
}

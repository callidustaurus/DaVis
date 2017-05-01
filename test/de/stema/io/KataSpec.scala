package de.stema.io

import org.scalatestplus.play.PlaySpec

class Kerze(val brennstärke: Int)
object Kerze {
  def unapply(kerze: Kerze): Option[Int] = Some(kerze.brennstärke)
}
case class Glühkerze(override val brennstärke: Int) extends Kerze(brennstärke)
case class Zündkerze(override val brennstärke: Int) extends Kerze(brennstärke)

class Motor(val ps: Int, val kerze: Kerze)
object Motor {
  def unapply(motor: Motor): Option[(Int, Kerze)] = Option((motor.ps, motor.kerze))
}
case class DieselMotor(dieslePs: Int, glühkerze: Glühkerze) extends Motor(dieslePs, glühkerze)
case class BenzinMotor(override val ps: Int, zündkerze: Zündkerze) extends Motor(ps, zündkerze)
case class HybridMotor(override val ps: Int, hybridKerze: Kerze) extends Motor(ps, hybridKerze)

class Auto(val motor: Motor)
object Auto {
  def unapply(auto: Auto): Option[Motor] = Some(auto.motor)
}
case class BMW(override val motor: Motor) extends Auto(motor)
case class Audi(override val motor: Motor) extends Auto(motor)

class KataSpec extends PlaySpec {

  val dieselAudi = Audi(DieselMotor(180, Glühkerze(300)))
  val benzinBWM = BMW(BenzinMotor(300, Zündkerze(150)))
  val hybridMotorGlüh = Audi(HybridMotor(120, Glühkerze(100)))
  val hybridMotorZünd = Audi(HybridMotor(120, Zündkerze(100)))

  "brennstärke von hybridMotor" should {
    "nur returned werden" when {
      "es ne glühkerze is" in {
        (hybridMotorGlüh match {
          case Auto(Motor(_, kerze: Glühkerze)) => kerze.brennstärke
          case _ => 0
        }) mustBe 100
      }
    }

    "es nen Benzinmotor is" in {
      (benzinBWM match {
        case Auto(BenzinMotor(_, kerze)) => kerze.brennstärke
        case _ => 0
      }) mustBe 150
    }

    "es nen audi is mit genau 180PS" in {
      (dieselAudi match {
        case Audi(Motor(180, kerze)) => kerze.brennstärke
        case _ => 0
      }) mustBe 300
    }
  }
}

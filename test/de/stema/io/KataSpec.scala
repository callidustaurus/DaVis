package de.stema.io

import java.lang.Integer

import org.scalatestplus.play.PlaySpec

class Kerze(val brennstärke: Integer)

object Kerze {
  def unapply(kerze: Kerze): Option[Integer] = Some(kerze.brennstärke)
}

case class Glühkerze(override val brennstärke: Integer) extends Kerze(brennstärke)

case class Zündkerze(override val brennstärke: Integer) extends Kerze(brennstärke)

class Motor(val ps: Integer, val kerze: Kerze)

object Motor {
  def unapply(motor: Motor): Option[(Integer, Kerze)] = Option((motor.ps, motor.kerze))
}

case class DieselMotor(dieslePs: Integer, glühkerze: Glühkerze) extends Motor(dieslePs, glühkerze)

case class BenzinMotor(override val ps: Integer, zündkerze: Zündkerze) extends Motor(ps, zündkerze)

case class HybridMotor(override val ps: Integer, hybridKerze: Kerze) extends Motor(ps, hybridKerze)

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
       // case Audi(Motor(180, kerze)) => kerze.brennstärke
        case _ => 0
      }) mustBe 300
    }
  }

  "exceptions" should {
    "be thrown in case of null" in {
      def validate(auto: Auto) = auto match {
        case null                           => throw NullAutoException("Auto ist null")
        case Auto(null)                     => throw NullMotorException("Motor ist null")
        case Auto(Motor(null, null))        => throw NullPsUndNullKerze("ps und kerze sind null")
        case Auto(Motor(null, Kerze(null))) => throw NullPsUndNullBrennwert("ps sind null")
        case Auto(Motor(null, _))           => throw NullPsException("ps sind null")
        case Auto(Motor(_, null))           => throw NullKerzeException("Kerze is null")
        case Auto(Motor(_, Kerze(null)))    => throw NullBrennwert("Brennstärke ist null")
        case _ => auto
      }

      val nullAuto                     = null
      val audiMitNullMotor             = Audi(null)
      val bmwMitNullKerze              = BMW(DieselMotor(100, null))
      val bmwMitNullPs                 = BMW(BenzinMotor(null, Zündkerze(100)))
      val audiMitNullPsUndNullKerze    = Audi(DieselMotor(null, null))
      val autoMitNullBrennwert         = new Auto(new Motor(120, new Kerze(null)))
      val bmwMitNullPsUndNullBrennwert = BMW(new Motor(null, new Kerze(null)))

      intercept[  NullAutoException]      (validate(  nullAuto))
      intercept[  NullMotorException]     (validate(  audiMitNullMotor))
      intercept[  NullPsException]        (validate(  bmwMitNullPs))
      intercept[  NullKerzeException]     (validate(  bmwMitNullKerze))
      intercept[  NullPsUndNullKerze]     (validate(  audiMitNullPsUndNullKerze))
      intercept[  NullBrennwert]          (validate(  autoMitNullBrennwert))
      intercept[  NullKerzeException]     (validate(  bmwMitNullKerze))
      intercept[  NullPsUndNullBrennwert] (validate(  bmwMitNullPsUndNullBrennwert))
    }

    "be thrown in case of wrong type" in {
      new Auto(null) match {
        case auto: BMW => throw new RuntimeException
      }
    }
  }
}

case class NullAutoException(msg: String) extends IllegalArgumentException(msg)
case class NullMotorException(msg: String) extends IllegalArgumentException(msg)
case class NullPsException(msg: String) extends IllegalArgumentException(msg)
case class NullKerzeException(msg: String) extends IllegalArgumentException(msg)
case class NullPsUndNullKerze(msg: String) extends IllegalArgumentException(msg)
case class NullBrennwert(msg: String) extends IllegalArgumentException(msg)
case class NullPsUndNullBrennwert(msg: String) extends IllegalArgumentException(msg)

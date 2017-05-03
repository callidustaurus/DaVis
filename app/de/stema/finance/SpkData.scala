package de.stema.finance

import java.time.LocalDate

import de.stema.io.{CSVData, Config}

class SpkData extends CategoryMapper with Config {
  @CSVData("Auftragskonto")                      val aufragsKonto: BigInt = null
  @CSVData("Buchungstag")                        val buchungsTag: LocalDate = null
  @CSVData("Valutadatum")                        val valutaDatum: LocalDate = null
  @CSVData("Buchungstext")                       val buchungsText: String = null
  @CSVData("Verwendungszweck")                   val verwendungszweck: String = null
  @CSVData("Glaeubiger ID")                      val glaeubigerID: String = null
  @CSVData("Mandatsreferenz")                    val mandatsReferenz: String = null
  @CSVData("Kundenreferenz (End-to-End)")        val kundenReferenz: String = null
  @CSVData("Sammlerreferenz")                    val sammlerReferenz: String = null
  @CSVData("Lastschrift Ursprungsbetrag")        val ursprungsBetrag: String = null
  @CSVData("Auslagenersatz Ruecklastschrift")    val auslagenErsatzRuecklastschrift: String = null
  @CSVData("Beguenstigter/Zahlungspflichtiger")  val beguenstigter: String = null
  @CSVData("Kontonummer/IBAN")                   val iban: String = null
  @CSVData("BIC (SWIFT-Code)")                   val bic: String = null
  @CSVData("Betrag")                             val betrag: BigDecimal = null
  @CSVData("Waehrung")                           val waehrung: String = null
  @CSVData("Info")                               val info: String = null

  protected def valueToMap: String = glaeubigerID

  protected def mapping: Map[String, PayedForCategory] = Map(
    "Lidl" -> Lebensmittel,
    "rewe" -> Lebensmittel,
    "VHV"  -> Versicherung
  )
}

package de.stema.finance

import de.stema.io.CSVData

class SpkData {
  @CSVData("Auftragskonto") val konto: String = null
  @CSVData("Buchungstag") val buchungstag: String = null
  @CSVData("Verwendungszweck") val verwendungszweck: String = null
  @CSVData("Betrag") val betrag: String = null
}

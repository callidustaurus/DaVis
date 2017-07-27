package de.stema.finance


sealed trait PayedForCategory {
  val name: String
}

// TODO categories should be provided by a "dynamic" configuration file
case object Versicherung extends PayedForCategory {override val name = "Versicherung"}
case object Auto extends PayedForCategory {override val name = "Auto"}
case object Lebensmittel extends PayedForCategory {override val name = "Lebensmittel"}
case object Wohnen extends PayedForCategory {override val name = "Wohnung/Haus"}
case object Urlaub extends PayedForCategory {override val name = "Urlaub"}
case object Geschenke extends PayedForCategory {override val name = "Geschenke"}
case object Einkommen extends PayedForCategory {override val name = "Einkommen"}
case object Luxus extends PayedForCategory {override val name = "Luxus"}
case object Sonstiges extends PayedForCategory { override val name = "Sonstiges"}

trait CategoryMapper {
  protected def valueToMap : String
  protected def mapping : Map[String, PayedForCategory]

  def getCategory: PayedForCategory = {
    val category = mapping.keySet.find { key =>
      valueToMap.toLowerCase.contains(key.toLowerCase)
    }.map (mapping)

    category.getOrElse(Sonstiges)
  }
}

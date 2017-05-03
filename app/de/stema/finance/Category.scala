package de.stema.finance


sealed trait PayedForCategory {
  val name: String
}

case object Versicherung extends PayedForCategory {override val name = "Versicherung"}
case object Auto extends PayedForCategory {override val name = "Auto"}
case object Lebensmittel extends PayedForCategory {override val name = "Lebensmittel"}
case object Wohnen extends PayedForCategory {override val name = "Wohnung/Haus"}
case object Urlaub extends PayedForCategory {override val name = "Urlaub"}
case object Geschenke extends PayedForCategory {override val name = "Geschenke"}
case object Sonstiges extends PayedForCategory { override val name = "Sonstiges"}

trait CategoryMapper {
  protected def valueToMap : String
  protected def mapping : Map[String, PayedForCategory]

  def category: PayedForCategory = {
    mapping.keySet.map { key =>
      if (valueToMap.toLowerCase.contains(key.toLowerCase))
        mapping(key)
      else None
        .getOrElse(Sonstiges)
    }.head
  }
}

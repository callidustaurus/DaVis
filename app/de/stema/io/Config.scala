package de.stema.io

import play.Configuration


private[io] object Config {
  val CONFIG_KEY= "play-configuration"
}

trait Config {
  @CSVData(Config.CONFIG_KEY) val config: Configuration = null
}

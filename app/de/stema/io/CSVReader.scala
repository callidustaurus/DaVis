package de.stema.io


import scala.io.Source

object CSVReader {
  val DEFAULT_SEPARATOR = ";"

  def read(path: String, separator: String = DEFAULT_SEPARATOR): List[Map[String, String]] = {
    def splitLine(line: String): List[String] =
      line.split(separator).map(_.trim).toList

    // "public/csv/finance.csv"
    val bufferedSource = Source.fromFile(path)

    val data = bufferedSource.getLines().toList
    val header = splitLine(data.head)
    val content =
      for (
        line <- data.drop(1)
      ) yield (
          header zip splitLine(line)
          ).toMap

    bufferedSource.close

    content
  }
}

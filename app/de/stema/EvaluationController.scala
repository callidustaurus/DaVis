package de.stema

import javax.inject.Inject

import de.stema.finance.SpkData
import de.stema.io.{CSVDataMapper, CSVReader}
import play.api.mvc.{Action, Controller}

/**
  * Created by stefan on 25.07.17.
  */
class EvaluationController @Inject()(dataMapper: CSVDataMapper) extends Controller {

  def showListedData = Action {
    val csvData = CSVReader.read("public/csv/finance.csv")

    val results = dataMapper.transformDataTo[SpkData](csvData)
    Ok(de.stema.html.list(results))
  }
}

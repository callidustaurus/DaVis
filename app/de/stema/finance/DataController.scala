package de.stema.finance

import javax.inject.Inject

import de.stema.io.{CSVDataMapper, CSVReader, JsonObjectMapper}
import play.api.Configuration
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, AnyContent, Controller}
import scala.concurrent.ExecutionContext.Implicits.global

class DataController @Inject()(ws:WSClient, dataMapper: CSVDataMapper, jsonMapper: JsonObjectMapper, configuration: Configuration) extends Controller {

  // TODO create a form for a fileupload
  private def filePath = configuration.getString("pathToCSV").getOrElse(
    throw new IllegalArgumentException("path to CSV-file missing. Provide it on app-start with -D option"))

  def list: Action[AnyContent] = Action {
    Ok(de.stema.finance.html.datalist())
  }

  def getData: Action[AnyContent] = Action {
    val csvData = CSVReader.read(filePath)

    val results = dataMapper.transformDataTo[SpkData](csvData)
    val dataAsJson = jsonMapper.getJson(results)
    Ok(dataAsJson)
  }

  // TODO the elastic import should be more "abstract", move the elastic specific stuff so dedicated component
  def writeJson(path: String): Action[AnyContent] = Action.async {
    val csvData = CSVReader.read(filePath)

    val indexSplitter = """{"index":{"_index":"finance"}}"""

    val results = dataMapper.transformDataTo[SpkData](csvData)
    val dataAsJson = indexSplitter + "\n" + results.map { r =>
      jsonMapper.getJson(r).replaceAll("\n", "")
    }.mkString("\n" + indexSplitter + "\n")

    // TODO remove static url
    ws.url("http://localhost:9200/finance/buchung/_bulk").post(dataAsJson).map { respone =>
      Ok(respone.body)
    }
  }
}

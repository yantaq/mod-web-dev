package services

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter
import model.SunInfo
import play.api.libs.ws.WSClient
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SunService(ws: WSClient) {
  def getSunInfo(lat: Double, lon: Double): Future[SunInfo] = {
    val responseF = ws.url("http://api.sunrise-sunset.org/json?" + s"lat=$lat&lng=$lon&formatted=0").get()
    responseF.map { response =>
      val json = response.json
      val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
      val sunsetTimeStr = (json \ "results" \ "sunset").as[String]
      val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
      val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss").
        withZone(ZoneId.of("US/Pacific"))
      val sunInfo = SunInfo(sunriseTime.format(formatter),
        sunsetTime.format(formatter))
      sunInfo
    } }
}

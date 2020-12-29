package controllers

import java.time.{ZoneId, ZonedDateTime}
import java.time.format.DateTimeFormatter

import controllers.Assets.Asset
import javax.inject._
import model.SunInfo
import play.api.mvc._
import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {
  def index = Action.async {
      val responseF = ws.url("http://api.sunrise-sunset.org/json?" +
        "lat=37.6624312&lng=-121.8746789&formatted=0").get()
      responseF map { response =>
        val json = response.json
        val sunriseTimeStr = (json \ "results" \ "sunrise").as[String]
        val sunsetTimeStr = (json \ "results" \ "sunset").as[String]
        val sunriseTime = ZonedDateTime.parse(sunriseTimeStr)
        val sunsetTime = ZonedDateTime.parse(sunsetTimeStr)
        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
          .withZone(ZoneId.of("US/Pacific"))
        val sunInfo = SunInfo(sunriseTime.format(formatter), sunsetTime.format(formatter))
        Ok(views.html.index(sunInfo))
    }
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}



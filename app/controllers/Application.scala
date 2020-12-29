package controllers

import controllers.Assets.Asset
import javax.inject._
import play.api.mvc._
import play.api.libs.ws.WSClient
import services.{SunService, WeatherService}
import scala.concurrent.ExecutionContext.Implicits.global


class Application @Inject() (components: ControllerComponents, assets: Assets, ws: WSClient)
    extends AbstractController(components) {
  val sunService = new SunService(ws)
  val weatherService = new WeatherService(ws)

  def index = Action.async {
    val lat = 37.66
    val lon = -121.87
    val sunInfoF = sunService.getSunInfo(lat, lon)
    val temperatureF = weatherService.getTemperature(lat, lon)
    for {
      sunInfo <- sunInfoF
      weatherInfo <- temperatureF
    } yield {
      Ok(views.html.index(sunInfo, weatherInfo))
    }
  }

  def versioned(path: String, file: Asset) = assets.versioned(path, file)
}



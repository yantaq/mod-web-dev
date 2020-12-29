package services

import play.api.libs.ws.WSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WeatherService(ws: WSClient) {
  def getTemperature(lat: Double, lon: Double): Future[Double] = {

    val appid = sys.env.get("WEATHER_APP_ID")
    val weatherResponseF = ws.url("http://api.openweathermap.org/data/2.5/weather?" +
      s"lat=$lat&lon=$lon&units=metric&" +
      s"appid=$appid").get()

    weatherResponseF.map { weatherResponse =>
      val weatherJson = weatherResponse.json
      val temprature = (weatherJson \ "main" \ "temp").as[Double]
      temprature
    }
  }
}

import controllers.AssetsComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator, Logging}
import play.api.ApplicationLoader.Context
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.{ControllerComponents, DefaultControllerComponents, Filter}
import play.api.routing.Router
import router.Routes
import play.filters.HttpFiltersComponents
import com.softwaremill.macwire._
import controllers.Application
import filters.StatsFilter
import services.{SunService, WeatherService}

import scala.concurrent.Future

class AppLoader extends ApplicationLoader {
  def load(context: Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach { cfg =>
      cfg.configure(context.environment)
    }
    new AppComponents(context).application
  }
}

class AppComponents(context: Context)
  extends BuiltInComponentsFromContext(context)
    with AhcWSComponents
    with AssetsComponents
    with HttpFiltersComponents
    with Logging {

  override lazy val controllerComponents: ControllerComponents = wire[DefaultControllerComponents]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController = wire[Application]

  lazy val sunService = wire[SunService]
  lazy val weatherService = wire[WeatherService]

  // app start/stop hood
  applicationLifecycle.addStopHook { () =>
    logger.info("The app is about to stop")
    Future.successful(0)
  }

  val onStart = {
    logger.info("The app is about to start")
  }

  // Filters
  lazy val statsFilter : Filter = wire[StatsFilter]
  override lazy val httpFilters = Seq(statsFilter)
}

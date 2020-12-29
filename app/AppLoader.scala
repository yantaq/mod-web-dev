import controllers.AssetsComponents
import play.api.{ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import play.api.ApplicationLoader.Context
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.mvc.{ControllerComponents, DefaultControllerComponents}
import play.api.routing.Router
import router.Routes
import play.filters.HttpFiltersComponents
import com.softwaremill.macwire._
import controllers.Application

class AppLoader extends ApplicationLoader{
  def load(context: Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach { cfg =>
      cfg.configure(context.environment)
    }
    new AppComponents(context).application
  }
}

class AppComponents(context: Context) extends BuiltInComponentsFromContext(context) with AhcWSComponents
  with AssetsComponents with HttpFiltersComponents {
  override lazy val controllerComponents: ControllerComponents = wire[DefaultControllerComponents]
  lazy val prefix: String = "/"
  lazy val router: Router = wire[Routes]
  lazy val applicationController = wire[Application]

}

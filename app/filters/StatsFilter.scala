package filters

import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.Logging
import play.api.mvc.{Filter, RequestHeader, Result}

import scala.concurrent.Future

class StatsFilter(actorSystem: ActorSystem, implicit val mat: Materializer)
    extends Filter with Logging {

  override def apply(nextFilter: RequestHeader => Future[Result])(rh: RequestHeader): Future[Result] = {
    logger.info(s"Serving another request: ${rh.path}")
    nextFilter(rh)
  }

}

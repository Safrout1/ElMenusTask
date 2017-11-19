package v1.restaurant

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger, MarkerContext}

import scala.concurrent.Future

// final case class RestaurantData(id: RestaurantId, title: String, body: String)
final case class RestaurantData(uuid: String, enName: String, arName: String, state: String, routingMethod: String, logo: String, coverPhoto: String, enDescription: String, arDescription: String, shortNumber: String, facebookLink: String, twitterLink: String, youtubeLink: String, website: String, onlinePayment: Boolean, client: Boolean, pendingInfo: Boolean, pendingMenu: Boolean, closed: Boolean)

class RestaurantId private (val underlying: Int) extends AnyVal {
  override def toString: String = underlying.toString
}

object RestaurantId {
  def apply(raw: String): RestaurantId = {
    require(raw != null)
    new RestaurantId(Integer.parseInt(raw))
  }
}


class RestaurantExecutionContext @Inject()(actorSystem: ActorSystem) extends CustomExecutionContext(actorSystem, "repository.dispatcher")

/**
  * A pure non-blocking interface for the PostRepository.
  */
trait RestaurantRepository {
  def create(data: RestaurantData)(implicit mc: MarkerContext): Future[String]

  def list()(implicit mc: MarkerContext): Future[Iterable[RestaurantData]]

  def get(uuid: String)(implicit mc: MarkerContext): Future[Option[RestaurantData]]
}

/**
  * A trivial implementation for the Post Repository.
  *
  * A custom execution context is used here to establish that blocking operations should be
  * executed in a different thread than Play's ExecutionContext, which is used for CPU bound tasks
  * such as rendering.
  */
@Singleton
class RestaurantRepositoryImpl @Inject()()(implicit ec: RestaurantExecutionContext) extends RestaurantRepository {

  private val logger = Logger(this.getClass)

  private val postList = List(
    RestaurantData("5d81a479-add9-11e7-b988-0242ac110002", "3al Ahwa Cafe", "عالقهوة كافيه", "PUBLISHED", null, "i3qf6gym1p833di.jpg", null, "", null, "", "", "", "", null, false, false, true, true, false)
    //RestaurantData(RestaurantId("2"), "title 2", "blog post 2"),
    //RestaurantData(RestaurantId("3"), "title 3", "blog post 3"),
    //RestaurantData(RestaurantId("4"), "title 4", "blog post 4"),
    //RestaurantData(RestaurantId("5"), "title 5", "blog post 5")
  )

  override def list()(implicit mc: MarkerContext): Future[Iterable[RestaurantData]] = {
    Future {
      logger.trace(s"list: ")
      postList
    }
  }

  override def get(uuid: String)(implicit mc: MarkerContext): Future[Option[RestaurantData]] = {
    Future {
      logger.trace(s"get: uuid = $uuid")
      postList.find(post => (post.uuid).equals(uuid))
    }
  }

  def create(data: RestaurantData)(implicit mc: MarkerContext): Future[String] = {
    Future {
      logger.trace(s"create: data = $data")
      data.uuid
    }
  }

}

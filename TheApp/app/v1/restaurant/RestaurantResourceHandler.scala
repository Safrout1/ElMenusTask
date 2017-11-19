package v1.restaurant

import javax.inject.{Inject, Provider}

import play.api.MarkerContext

import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json._

/**
  * DTO for displaying post information.
  */
case class RestaurantResource(uuid: String, enName: String, arName: String, state: String, routingMethod: String, logo: String, coverPhoto: String, enDescription: String, arDescription: String, shortNumber: String, facebookLink: String, twitterLink: String, youtubeLink: String, website: String, onlinePayment: Boolean, client: Boolean, pendingInfo: Boolean, pendingMenu: Boolean, closed: Boolean)

object RestaurantResource {

  /**
    * Mapping to write a PostResource out as a JSON value.
    */
  implicit val implicitWrites = new Writes[RestaurantResource] {
    def writes(post: RestaurantResource): JsValue = {
      Json.obj(
        "uuid" -> post.uuid,
        "enName" -> post.enName,
        "arName" -> post.arName,
        "state" -> post.state,
        "routingMethod" -> post.routingMethod,
        "logo" -> post.logo,
        "coverPhoto" -> post.coverPhoto,
        "enDescription" -> post.enDescription,
        "arDescription" -> post.arDescription,
        "shortNumber" -> post.shortNumber,
        "facebookLink" -> post.facebookLink,
        "twitterLink" -> post.twitterLink,
        "youtubeLink" -> post.youtubeLink,
        "website" -> post.website,
        "onlinePayment" -> post.onlinePayment,
        "client" -> post.client,
        "pendingInfo" -> post.pendingInfo,
        "pendingMenu" -> post.pendingMenu,
        "closed" -> post.closed
      )
    }
  }
}

/**
  * Controls access to the backend data, returning [[PostResource]]
  */
class RestaurantResourceHandler @Inject()(
    routerProvider: Provider[RestaurantRouter],
    postRepository: RestaurantRepository)(implicit ec: ExecutionContext) {

  def create(postInput: RestaurantFormInput)(implicit mc: MarkerContext): Future[RestaurantResource] = {
    val data = RestaurantData(postInput.uuid, postInput.enName, postInput.arName, postInput.state, postInput.routingMethod, postInput.logo, postInput.coverPhoto, postInput.enDescription, postInput.arDescription, postInput.shortNumber, postInput.facebookLink, postInput.twitterLink, postInput.youtubeLink, postInput.website, postInput.onlinePayment, postInput.client, postInput.pendingInfo, postInput.pendingMenu, postInput.closed)
    // We don't actually create the post, so return what we have
    postRepository.create(data).map { id =>
      createPostResource(data)
    }
  }

  def lookup(uuid: String)(implicit mc: MarkerContext): Future[Option[RestaurantResource]] = {
    val postFuture = postRepository.get(uuid)
    postFuture.map { maybePostData =>
      maybePostData.map { postData =>
        createPostResource(postData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[RestaurantResource]] = {
    postRepository.list().map { postDataList =>
      postDataList.map(postData => createPostResource(postData))
    }
  }

  private def createPostResource(p: RestaurantData): RestaurantResource = {
    RestaurantResource(p.uuid, p.enName, p.arName, p.state, p.routingMethod, p.logo, p.coverPhoto, p.enDescription, p.arDescription, p.shortNumber, p.facebookLink, p.twitterLink, p.youtubeLink, p.website, p.onlinePayment, p.client, p.pendingInfo, p.pendingMenu, p.closed)
  }

}

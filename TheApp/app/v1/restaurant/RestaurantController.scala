package v1.restaurant

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class RestaurantFormInput(uuid: String, enName: String, arName: String, state: String, routingMethod: String, logo: String, coverPhoto: String, enDescription: String, arDescription: String, shortNumber: String, facebookLink: String, twitterLink: String, youtubeLink: String, website: String, onlinePayment: Boolean, client: Boolean, pendingInfo: Boolean, pendingMenu: Boolean, closed: Boolean)

/**
  * Takes HTTP requests and produces JSON.
  */
class RestaurantController @Inject()(cc: RestaurantControllerComponents)(implicit ec: ExecutionContext)
    extends RestaurantBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[RestaurantFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "uuid" -> text,
        "enName" -> text,
        "arName" -> text,
        "state" -> text,
        "routingMethod" -> text,
        "logo" -> text,
        "coverPhoto" -> text,
        "enDescription" -> text,
        "arDescription" -> text,
        "shortNumber" -> text,
        "facebookLink" -> text,
        "twitterLink" -> text,
        "youtubeLink" -> text,
        "website" -> text,
        "onlinePayment" -> boolean,
        "client" -> boolean,
        "pendingInfo" -> boolean,
        "pendingMenu" -> boolean,
        "closed" -> boolean
      )(RestaurantFormInput.apply)(RestaurantFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = RestaurantAction.async { implicit request =>
    logger.trace("index: ")
    RestaurantResourceHandler.find.map { posts =>
      Ok(Json.toJson(posts))
    }
  }

  def process: Action[AnyContent] = RestaurantAction.async { implicit request =>
    logger.trace("process: ")
    processJsonPost()
  }

  def show(id: String): Action[AnyContent] = RestaurantAction.async { implicit request =>
    logger.trace(s"show: id = $id")
    RestaurantResourceHandler.lookup(id).map { post =>
      Ok(Json.toJson(post))
    }
  }

  private def processJsonPost[A]()(implicit request: RestaurantRequest[A]): Future[Result] = {
    def failure(badForm: Form[RestaurantFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: RestaurantFormInput) = {
      RestaurantResourceHandler.create(input).map { post =>
        Created(Json.toJson(post))
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}

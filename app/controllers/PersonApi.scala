package controllers

import models.PersonPm
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsError
import play.api.mvc._

import scala.concurrent.Future

class PersonApi extends Controller {

  def register1 = Action(parse.json) { implicit request =>
    request.body.validate[PersonPm].map { person =>
      Ok("Has registered.")
    }.recoverTotal { error =>
      val errorJSON = JsError.toJson(e = error)
      BadRequest(errorJSON)
    }
  }

  def register2 = Action.async(parse.json) { implicit request =>
    val futurePerson = Future {
      request.body.validate[PersonPm].map { person =>
        person
      }.recoverTotal { error =>
        JsError.toJson(e = error)
      }
    }

    val timeoutFuture = play.api.libs.concurrent.Promise.timeout("Oops", 1)
    Future.firstCompletedOf(Seq(futurePerson, timeoutFuture)).map {
      case p: PersonPm => Ok("Has registered.")
      case t: String => InternalServerError(t)
    }
  }

}

package controllers

import models.PersonDto
import play.api.libs.json.JsError
import play.api.mvc._

class PersonApi extends Controller {

  def register1 = Action(parse.json) { implicit request =>
    request.body.validate[PersonDto].map { person =>
      Ok("Has registered.")
    }.recoverTotal { error =>
      val errorJSON = JsError.toJson(e = error)
      BadRequest(JsError.toJson(e = error))
    }
  }

  def register2 = Action(parse.json) { implicit request =>
    request.body.validate[PersonDto].map { person =>
      Ok("Has registered.")
    }.recoverTotal { error =>
      val errorJSON = JsError.toJson(e = error)
      BadRequest(JsError.toJson(e = error))
    }
  }

}

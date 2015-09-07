package controllers

import models.PersonDto
import play.api.libs.json.JsError
import play.api.mvc._

class PersonApi extends Controller {

  def add = Action(parse.json) { implicit request =>
    request.body.validate[PersonDto].map { person =>
      Ok("登録完了")
    }.recoverTotal { error =>
      val errorJSON = JsError.toJson(e = error)
      BadRequest(JsError.toJson(e = error))
    }
  }

}

package controllers

import models.PersonDto
import play.api.libs.json.JsError
import play.api.mvc._

class PersonApi extends Controller {

  /**
   * 追加
   * @return
   */
  def add = Action(parse.json) { implicit request =>
    request.body.validate[PersonDto].map { person =>
      Ok("登録完了")
    }.recoverTotal { errors =>
//      BadRequest(JsError.toJson(e = errors))
      BadRequest("親切なエラーメッセージ")
    }
  }

}

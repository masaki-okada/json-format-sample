package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, _}

case class NameDto(firstName: String, middleName: Option[String], lastName: String)

object NameDto {
  implicit val formatter: Format[NameDto] = (
    (__ \ "firstName").format[String](minLength[String](2) <~ maxLength[String](20)) and
      (__ \ "middleName").formatNullable[String] and
      (__ \ "lastName").format[String](minLength[String](2) <~ maxLength[String](20))
    )(NameDto.apply, unlift(NameDto.unapply))
}

package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, _}

case class NameDto(firstName: String, middleName: Option[String], lastName: String)

object NameDto {
  implicit val formatter: Format[NameDto] = (
    (__ \ "firstName").format[String] and
      (__ \ "middleName").formatNullable[String] and
      (__ \ "lastName").format[String]
    )(NameDto.apply, unlift(NameDto.unapply))
}

package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, _}

case class NameDto(firstName: String, middleName: Option[String], lastName: String)

object NameDto {

  implicit val reader: Reads[NameDto] = (
    (__ \ "firstName").read[String](minLength[String](2) <~ maxLength[String](20)) and
      (__ \ "middleName").readNullable[String](maxLength[String](20)) and
      (__ \ "lastName").read[String](minLength[String](2) <~ maxLength[String](20))
    )(NameDto.apply _)

  implicit val writer: Writes[NameDto] = (
    (__ \ "firstName").write[String] and
      (__ \ "middleName").writeNullable[String] and
      (__ \ "lastName").write[String]
    )(unlift(NameDto.unapply))

  implicit val formatter: Format[NameDto] = Format(reader, writer)
}

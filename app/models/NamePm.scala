package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Format, _}

case class NamePm(firstName: String, middleName: Option[String], lastName: String)

object NamePm {

  implicit val reader: Reads[NamePm] = (
    (__ \ "firstName").read[String](minLength[String](2) <~ maxLength[String](20)) and
      (__ \ "middleName").readNullable[String](maxLength[String](20)) and
      (__ \ "lastName").read[String](minLength[String](2) <~ maxLength[String](20))
    )(NamePm.apply _)

  implicit val writer: Writes[NamePm] = (
    (__ \ "firstName").write[String] and
      (__ \ "middleName").writeNullable[String] and
      (__ \ "lastName").write[String]
    )(unlift(NamePm.unapply))

  implicit val formatter: Format[NamePm] = Format(reader, writer)
}

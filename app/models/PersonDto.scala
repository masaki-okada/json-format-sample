package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

case class PersonDto(age: Int, name: NameDto)

object PersonDto {
  implicit val formatter: Format[PersonDto] = (
    (__ \ "age").format[Int] and
      (__ \ "name").format[NameDto]
    )(PersonDto.apply _, unlift(PersonDto.unapply))
}
package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import scala.util.matching.Regex

case class PersonDto(
                      age: Int
                      , bloodType: Option[String]
                      , name: NameDto
                      )

object PersonDto {
  implicit val reader: Reads[PersonDto] = (
    (__ \ "age").read[Int](min(0) <~ max(200)) and
      (__ \ "bloodType").readNullable[String](pattern(regex = new Regex("""^[A|B|O|AB]{1}$"""), error = "")) and
      (__ \ "name").read[NameDto]
    )(PersonDto.apply _)

  implicit val writer: Writes[PersonDto] = (
    (__ \ "age").write[Int] and
      (__ \ "bloodType").writeNullable[String] and
      (__ \ "name").write[NameDto]
    )(unlift(PersonDto.unapply))

  implicit val formatter: Format[PersonDto] = Format(reader, writer)
}
package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import scala.util.matching.Regex

case class PersonDto(
                      age: Int
                      , bloodType: Option[String]
                      , favoriteNumbers: Option[Seq[Int]]
                      , name: NameDto
                      )

object PersonDto {

  val bloodTypeRegex = new Regex("""^[A|B|O|AB]{1}$""")
  val bloodTypeErrorMessage = "A or B or O or AB"

  implicit val reader: Reads[PersonDto] = (
    (__ \ "age").read[Int](min(0) <~ max(200)) and
      (__ \ "bloodType").readNullable[String](pattern(regex = bloodTypeRegex, error = bloodTypeErrorMessage)) and
      (__ \ "favoriteNumbers").readNullable[Seq[Int]] and
      (__ \ "name").read[NameDto]
    )(PersonDto.apply _)

  implicit val writer: Writes[PersonDto] = (
    (__ \ "age").write[Int] and
      (__ \ "bloodType").writeNullable[String] and
      (__ \ "favoriteNumbers").writeNullable[Seq[Int]] and
      (__ \ "name").write[NameDto]
    )(unlift(PersonDto.unapply))

  implicit val formatter: Format[PersonDto] = Format(reader, writer)
}
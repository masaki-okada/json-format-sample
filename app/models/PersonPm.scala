package models

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

import scala.util.matching.Regex

case class PersonPm(
                      age: Int
                      , bloodType: Option[String]
                      , favoriteNumbers: Option[Seq[Int]]
                      , name: NamePm
                      )

object PersonPm {

  val bloodTypeRegex = new Regex("""^[A|B|O|AB]{1}$""")
  val bloodTypeErrorMessage = "A or B or O or AB"

  implicit val reader: Reads[PersonPm] = (
    (__ \ "age").read[Int](min(0) <~ max(200)) and
      (__ \ "bloodType").readNullable[String](pattern(regex = bloodTypeRegex, error = bloodTypeErrorMessage)) and
      (__ \ "favoriteNumbers").readNullable[Seq[Int]] and
      (__ \ "name").read[NamePm]
    )(PersonPm.apply _)

  implicit val writer: Writes[PersonPm] = (
    (__ \ "age").write[Int] and
      (__ \ "bloodType").writeNullable[String] and
      (__ \ "favoriteNumbers").writeNullable[Seq[Int]] and
      (__ \ "name").write[NamePm]
    )(unlift(PersonPm.unapply))

  implicit val formatter: Format[PersonPm] = Format(reader, writer)
}
package controllers

import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json._
import play.api.test.Helpers._
import play.api.test._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PersonApiSpec extends Specification {

  "PersonApi" should {

    "add 異常系" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/persion/add"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "aaa": 1
              | , "bbb": {
              |   "fName": "fName"
              |   , "lName": "lName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual BAD_REQUEST
      contentAsJson(result) mustEqual Json.parse(
        """
          |{
          |    "obj.age": [{"msg": ["error.path.missing"], "args": []}],
          |    "obj.name": [{"msg": ["error.path.missing"], "args": []}]
          |}
        """.stripMargin
      )
    }

    "add 正常系" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/persion/add"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 36
              | , "name": {
              |   "firstName": "fName"
              |   , "lastName": "lName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual OK
      contentAsString(result) mustEqual "登録完了"
    }

  }
}

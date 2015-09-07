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

  "PersonApi#add" should {
    val API = "/api/persion/add"

    "missing path error, age, name" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , API
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "aaa": 1
              | , "bbb": {
              |   "ccc": "firstName"
              |   , "ddd": "lastName"
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

    "max length error of middle name" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , API
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 1
              | , "name": {
              |   "firstName": "firstName"
              |   , "middleName": "012345678901234567890"
              |   , "lastName": "lastName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual BAD_REQUEST
      contentAsJson(result) mustEqual Json.parse(
        """
          |{
          |    "obj.name.middleName": [{
          |        "msg": ["error.maxLength"]
          |        , "args": [20]
          |    }]
          |}
        """.stripMargin
      )

    "success" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , API
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 36
              | , "name": {
              |   "firstName": "firstName"
              |   , "lastName": "lastName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual OK
      contentAsString(result) mustEqual "登録完了"
    }

    "append option type middle name" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , API
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 36
              | , "name": {
              |   "firstName": "firstName"
              |   , "middleName": "せばすちゃん"
              |   , "lastName": "lastName"
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

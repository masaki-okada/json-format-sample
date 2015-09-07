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

    "display json parse error caused by PersonDto(age, name are missing)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
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

    "display json parse error caused by NameDto(firstName and lastName are missing)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 1
              | , "name": {
              |   "ccc": "firstName"
              |   , "ddd": "middleName"
              |   , "eee": "lastName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual BAD_REQUEST
      contentAsJson(result) mustEqual Json.parse(
        """
          |{
          |    "obj.name.lastName": [{"msg": ["error.path.missing"], "args": []}],
          |    "obj.name.firstName": [{"msg": ["error.path.missing"], "args": []}]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by PersonDto(age is less than minimum value)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": -1
              | , "name": {
              |   "firstName": "firstName"
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
          |    "obj.age": [{
          |        "msg": ["error.min"]
          |        , "args": [0]
          |    }]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by PersonDto(age is more than maximum value)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 201
              | , "name": {
              |   "firstName": "firstName"
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
          |    "obj.age": [{
          |        "msg": ["error.max"]
          |        , "args": [200]
          |    }]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by PersonDto(blood type are A or B or O or AB)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 36
              | , "bloodType": "C"
              | , "name": {
              |   "firstName": "firstName"
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
          |    "obj.bloodType": [{
          |        "msg": ["A or B or O or AB"]
          |        , "args": []
          |    }]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by NameDto(first name is less than minimum length)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 1
              | , "name": {
              |   "firstName": ""
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
          |    "obj.name.firstName": [{
          |        "msg": ["error.minLength"]
          |        , "args": [2]
          |    }]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by NameDto(first name is more than maximum length)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 1
              | , "name": {
              |   "firstName": "012345678901234567890"
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
          |    "obj.name.firstName": [{
          |        "msg": ["error.maxLength"]
          |        , "args": [20]
          |    }]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by NameDto(last name is less than minimum length)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 1
              | , "name": {
              |   "firstName": "firstName"
              |   , "lastName": ""
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual BAD_REQUEST
      contentAsJson(result) mustEqual Json.parse(
        """
          |{
          |    "obj.name.lastName": [{
          |        "msg": ["error.minLength"]
          |        , "args": [2]
          |    }]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by NameDto(last name is more than maximum length)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 1
              | , "name": {
              |   "firstName": "firstName"
              |   , "lastName": "012345678901234567890"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual BAD_REQUEST
      contentAsJson(result) mustEqual Json.parse(
        """
          |{
          |    "obj.name.lastName": [{
          |        "msg": ["error.maxLength"]
          |        , "args": [20]
          |    }]
          |}
        """.stripMargin
      )
    }

    "display json parse error caused by NameDto(middle name is more than maximum length)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
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
    }

    "be success. If the no middle name." in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
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
      contentAsString(result) mustEqual "Has registered."
    }

    "be success. If the middle name exists." in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 36
              | , "name": {
              |   "firstName": "firstName"
              |   , "middleName": "middleName"
              |   , "lastName": "lastName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual OK
      contentAsString(result) mustEqual "Has registered."
    }

    // TODO: ループで全パターンやる
    "be success. If the blood type exists.(A)" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 36
              | , "bloodType": "A"
              | , "name": {
              |   "firstName": "firstName"
              |   , "lastName": "lastName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual OK
      contentAsString(result) mustEqual "Has registered."
    }

    "be success. If the favorite numbers exists." in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , "/api/v1/person/register"
          , FakeHeaders(Seq(CONTENT_TYPE -> "application/json"))
          , Json.parse(
            """
              |{
              | "age": 36
              | , "favoriteNumbers": [3, 7, 8]
              | , "name": {
              |   "firstName": "firstName"
              |   , "lastName": "lastName"
              | }
              |}
            """.stripMargin)
        )
      )
      status(result) mustEqual OK
      contentAsString(result) mustEqual "Has registered."
    }

  }
}
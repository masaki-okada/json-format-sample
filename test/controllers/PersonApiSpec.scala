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

    // パスエラー age, name
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

    // パスエラー
    "missing path error, name" in new WithApplication {
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

    // 年齢　最小値エラー
    "validation error caused by min of age" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , API
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

    // 年齢　最大値エラー
    "validation error caused by min of age" in new WithApplication {
      val Some(result) = route(
        FakeRequest(
          POST
          , API
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

    // 名前　最小文字数エラー
    "validation error caused by min length of first name" in new WithApplication {
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

    // 名前　最大文字数エラー
    "validation error caused by min length of first name" in new WithApplication {
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

    // 苗字　最小文字数エラー
    "validation error caused by min length of last name" in new WithApplication {
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

    // 名前　最大文字数エラー
    "validation error caused by min length of last name" in new WithApplication {
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

    // ミドルネーム　最大文字数エラー
    "validation error caused by max length of middle name" in new WithApplication {
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
    }

    // 正常系
    "add person" in new WithApplication {
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
}
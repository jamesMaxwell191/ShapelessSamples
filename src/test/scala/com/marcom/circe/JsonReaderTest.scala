package com.marcom.circe

import org.scalatest.Matchers
import shapeless.{HNil,HList,::}

/**
  * Created by douglasmaxwell on 27/08/2017.
  */
class JsonReaderTest extends org.scalatest.WordSpec with Matchers {

  "a json reader" should {
     "read a number" in {
        val reader = JsonReader[Int::HNil]
        val result = reader.decode(JsObject(List(("age",JsInteger(10)))))
       result should be(JsSuccess(10::HNil))
     }
    "read an IceCream" in {
      val reader = JsonReader[IceCream]
      val result = reader.decode(JsObject(List(("name",JsString("cornetto")),("cherries",JsInteger(10)))))
      result should be(JsSuccess(IceCream("cornetto",10)))
    }
  }

}

case class IceCream(name:String,cherries:Int)

package com.marcom.circe

import org.scalatest.{Matchers, WordSpec}

import JsOperations._


class JsonObjectEncoderTest extends WordSpec with Matchers {

  "a json object encoder" should {
     "encode" in {
        val addr = Address("1 The High Street","Streatham")
        val p = new Person("joe","soap",20,addr)
        val aj = JsObject(("street",JsString("1 The High Street"))::("city",JsString("Streatham"))::Nil)
        val expected = JsObject(("first",JsString("joe"))::("last",JsString("soap"))::("age",JsInteger(20))::("address",aj)::Nil)
        val actual = p.asJson
        actual should be(expected)
     }
  }

}

case class Person(first:String,last:String,age:Int,address:Address)

case class Address(street:String,city:String)

package com.marcom.serialization.csv

import org.scalatest.Matchers

import shapeless.{::, HNil, HList}

/**
  * Created by douglasmaxwell on 21/08/2017.
  */
class CsvDecoderTest extends org.scalatest.WordSpec with Matchers {

  "a csvdecoder" should {
    "decode a person from valid input" in {
       val dec = CsvDecoder[Person]
       val result = dec.decode(List("joe","soap","10"))
       result.getOrElse(Person("none","none",0) should be(Person("joe","soap",10)))
    }
    "decode a person from valid input again" in {
      val result = Csv.decode[Person](List("joe","soap","10"))
      result.getOrElse(Person("none","none",0) should be(Person("joe","soap",10)))
    }
    "report an error from invalid input" in {
      val result = Csv.decode[Person](List("joe","soap","ten"))
      (result.left.getOrElse(Person("none","none",0))) should be(Person("joe","soap",10))
    }
  }
}

case class Person(first:String,second:String,age:Int)

case class MyPoint(x:Int,y:Int)

case class MyCircle(radius:Int,centre:MyPoint)

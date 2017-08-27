package com.marcom.serialization.csv

import org.scalatest.Matchers

import shapeless.{::, HNil, HList}

/**
  * Created by douglasmaxwell on 21/08/2017.
  */
class CsvDecoderTest extends org.scalatest.WordSpec with Matchers {

  "a decoder" should {
    "do it" in {
       val dec = CsvDecoder[Person]
       val (joe,count) = dec.decode(List("joe","soap","10"))
       joe should be(Person("joe","soap",10))
    }
    "do it again" in {
      val dec = CsvDecoder[String::String::HNil]
      val (l,i) = dec.decode(List("joe","soap"))
      (l,i) should be(("joe"::"soap"::HNil,2))
    }
    "do it for a circle" in {
      val dec = CsvDecoder[MyCircle]
      val (joe,count) = dec.decode(List("10","5","5"))
      joe should be(MyCircle(10,MyPoint(5,5)))
    }
  }
}

case class Person(first:String,second:String,age:Int)

case class MyPoint(x:Int,y:Int)

case class MyCircle(radius:Int,centre:MyPoint)

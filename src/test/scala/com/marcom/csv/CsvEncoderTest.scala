package com.marcom.csv

import com.marcom.shapes.{Circle, Point, Shape}
import org.scalatest.{Matchers, WordSpec}
import shapeless.{::, HList, HNil}

/**
  * Created by douglasmaxwell on 07/08/2017.
  */
class CsvEncoderTest extends WordSpec with Matchers {

  "The CsvEncoder typeclass" should {
     "encode a list containing an int" in {
        val enc = CsvEncoder[Int::HNil]
        enc.encode(1::HNil) should be(List("1"))
     }
    "encode a composite list" in {
      val enc = CsvEncoder[Int::String::Boolean::HNil]
      enc.encode(1::"erica"::true::HNil) should be(List("1","erica","true"))
    }
    "encode a point" in {
      val enc = CsvEncoder[Point]
      enc.encode(Point(2,3)) should be(List("2","3"))
    }
    "encode a circle" in {
      val enc = CsvEncoder[Shape]
      enc.encode(Circle(Point(2,3),4)) should be(List("2","3","4"))
    }
  }

}

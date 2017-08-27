package com.marcom.ops.list

import org.scalatest.{Matchers, WordSpec}
import shapeless.{HNil,::,HList}


class LastTest extends WordSpec with Matchers {

  "the last typeclass" should {
    "return the last element of a single element HList" in {
      val list = "erica"::HNil
      val last = Last[String::HNil]
      last(list) should be("erica")
    }
    "return the last element of a multiple element HList" in {
      val list = 1::true::"erica"::HNil
      val last = Last[Int::Boolean::String::HNil]
      last(list) should be("erica")
    }
  }

}

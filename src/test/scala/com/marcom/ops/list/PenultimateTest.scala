package com.marcom.ops.list

import org.scalatest.{Matchers, WordSpec}
import shapeless.{HNil,::,HList}

/**
  * Created by douglasmaxwell on 07/08/2017.
  */
class PenultimateTest extends WordSpec with Matchers {

  "The Penultimate typeclass" should {
    "return the last but one element" in {
      val items = 1::true::"erica"::HNil
      val penultimate = Penultimate[Int::Boolean::String::HNil]
      penultimate(items) should be(true)
    }
  }

}

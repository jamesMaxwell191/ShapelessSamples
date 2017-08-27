package com.marcom.ops.list

import org.scalatest.{Matchers, WordSpec}
import shapeless.{HList,::,HNil}


class InitTest extends WordSpec with Matchers {

  "the Init typeclass" should {
    "return the initial elements of a HList" in {
        val items = 1::true::"erica"::HNil
        val init = Init[Int::Boolean::String::HNil]
        init(items) should be(1::true::HNil)
    }
  }

}

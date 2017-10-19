package com.marcom.flatten

import com.marcom.model.{Address, City, Person}
import org.scalatest.{Matchers, WordSpec}
import ConversionSyntax._



class FlattenTest extends WordSpec with Matchers{

  def typed[T](t: => T) = t

  "a test" should {
    "do the business" in {
      val t1 = (1, ((2, 3), 4))
      val f1 = flatten(t1)     // Inferred type is (Int, Int, Int, Int)
      println(f1)
    }
    "do the business again" in {
      val p = Person("joe","soap",Address("1 high street",City("york","YK")))
      val f = flatten(p)
      val pm = f.convertTo[PersonModel]
      println(pm)
    }
    "and do the business again" in {
      val p = Person("joe","soap",Address("1 high street",City("york","YK")))
      val g = ("sales","first")
      val f = flatten((g,p))
      val em = f.convertTo[ExPersonModel]
      println(em)
    }
  }

}

case class PersonModel(first:String,last:String,street:String,city:String,code:String)

case class ExPersonModel(dept:String,grade:String,first:String,last:String,street:String,city:String,code:String)

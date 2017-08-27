package com.marcom.circe

import shapeless.{::, HList, HNil}
import shapeless.labelled.FieldType
import shapeless.{HList, LabelledGeneric, Lazy, Witness}

sealed trait JsValue

case class JsString(value:String) extends JsValue

case class JsDouble(value:Double) extends JsValue

case class JsInteger(value:Int) extends JsValue

case class JsBoolean(value:Boolean) extends JsValue

case class JsArray(values:List[JsValue]) extends JsValue

case class JsObject(values:List[(String,JsValue)] = Nil) extends JsValue

case object JsNull extends JsValue

trait JsonEncoder[A] {

  def encode(a:A): JsValue

}

trait JsonObjectEncoder[A] extends JsonEncoder[A]{

  def encode(a:A): JsObject

}


object JsOperations{
  implicit class JsonOps[A](a:A){
    def asJson(implicit enc:JsonObjectEncoder[A]): JsValue = enc.encode(a)
  }
}

object JsonEncoder {

   def apply[A](implicit ev: JsonEncoder[A]): JsonEncoder[A] = ev

  def pure[A](f:A=>JsValue):JsonEncoder[A] = new JsonEncoder[A] {
    override def encode(a: A): JsValue = f(a)
  }

  implicit val stringEncoder:JsonEncoder[String] = pure {
     s => JsString(s)
  }
  implicit val intEncoder:JsonEncoder[Int] = pure {
    i => JsInteger(i)
  }

  def createObjectEncoder[A](f: A=> JsObject):JsonObjectEncoder[A] = new JsonObjectEncoder[A] {
    override def encode(a: A): JsObject = f(a)
  }

  implicit def jsonObjectEncoder[A,R](implicit gen: LabelledGeneric.Aux[A,R],
                                      enc:Lazy[JsonObjectEncoder[R]] ): JsonObjectEncoder[A] = createObjectEncoder{
    a => enc.value.encode(gen.to(a))
  }

  implicit val hnilEncoder:JsonObjectEncoder[HNil] = createObjectEncoder{ _ =>
     JsObject()
  }

  implicit def genericJsonObjectEncoder[K<:Symbol,H,T <: HList](implicit witness:Witness.Aux[K],
                                                                hEncoder:Lazy[JsonEncoder[H]],
                                                                tEncoder:JsonObjectEncoder[T]) : JsonObjectEncoder[FieldType[K,H]::T] = createObjectEncoder { list =>
    val  name = witness.value.name
    val h = hEncoder.value.encode(list.head)
    val t = tEncoder.encode(list.tail)
    JsObject((name,h) +: t.values)
  }

}


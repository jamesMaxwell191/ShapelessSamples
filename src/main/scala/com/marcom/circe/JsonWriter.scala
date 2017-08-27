package com.marcom.circe

import shapeless.{::, HList, HNil}
import shapeless.labelled.FieldType
import shapeless.{HList, LabelledGeneric, Lazy, Witness}



trait JsonWriter[A] {

  def encode(a:A): JsValue

}

trait JsonObjectWriter[A] extends JsonWriter[A]{

  def encode(a:A): JsObject

}


object JsOperations{
  implicit class JsonOps[A](a:A){
    def asJson(implicit enc:JsonObjectWriter[A]): JsValue = enc.encode(a)
  }
}

object JsonWriter {

   def apply[A](implicit ev: JsonWriter[A]): JsonWriter[A] = ev

  def pure[A](f:A=>JsValue):JsonWriter[A] = new JsonWriter[A] {
    override def encode(a: A): JsValue = f(a)
  }

  implicit val stringWriter:JsonWriter[String] = pure {
     s => JsString(s)
  }
  implicit val intWriter:JsonWriter[Int] = pure {
    i => JsInteger(i)
  }

  def createObjectWriter[A](f: A=> JsObject):JsonObjectWriter[A] = new JsonObjectWriter[A] {
    override def encode(a: A): JsObject = f(a)
  }

  implicit def jsonObjectWriter[A,R](implicit gen: LabelledGeneric.Aux[A,R],
                                      enc:Lazy[JsonObjectWriter[R]] ): JsonObjectWriter[A] = createObjectWriter{
    a => enc.value.encode(gen.to(a))
  }

  implicit val hnilWriter:JsonObjectWriter[HNil] = createObjectWriter{ _ =>
     JsObject()
  }

  implicit def genericJsonObjectWriter[K<:Symbol,H,T <: HList](implicit witness:Witness.Aux[K],
                                                                hWriter:Lazy[JsonWriter[H]],
                                                                tWriter:JsonObjectWriter[T]) : JsonObjectWriter[FieldType[K,H]::T] = createObjectWriter {
    case h::t =>
        val  name = witness.value.name
        val hj = hWriter.value.encode(h)
        val tj = tWriter.encode(t)
        JsObject((name,hj) +: tj.values)
  }

}


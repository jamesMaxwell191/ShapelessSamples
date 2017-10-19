package com.marcom.circe

import shapeless.{Generic,::,HNil, HList}

import JsResult.pure

sealed trait JsResult[+A]{

    def map[B](f:A => B):JsResult[B] = flatMap{ a =>
        pure(f(a))
    }

  def flatMap[B](f:A => JsResult[B]): JsResult[B] = this match {
    case JsError(e) => JsError(e)
    case JsSuccess(a) => f(a)
  }
}

case class JsSuccess[A](a:A) extends JsResult[A]

case class JsError(e:Throwable) extends JsResult[Nothing]

object JsResult {

   def pure[A](a:A): JsResult[A] = JsSuccess(a)
}


trait JsonReader[A] {

  def decode(json:JsValue): JsResult[A]
}

object JsonReader {

  def apply[A](implicit ev:JsonReader[A]): JsonReader[A] = ev

  def pure[A](f:JsValue => JsResult[A] ): JsonReader[A] = new JsonReader[A] {
    override def decode(json: JsValue): JsResult[A] = f(json)
  }

   implicit def decodeGeneric[A,R](
                             implicit gen:Generic.Aux[A,R],
                             reader:JsonReader[R]) : JsonReader[A] = pure {
        json => reader.decode(json).map(gen.from(_))
   }

   implicit val decodeString:JsonReader[String] = pure {
     case JsString(s) => JsSuccess(s)
     case _ => JsError(new IllegalArgumentException("invalid input"))
   }

  implicit val decodeInt:JsonReader[Int] = pure {
    case JsInteger(i) => JsSuccess(i)
    case _ => JsError(new IllegalArgumentException("invalid input"))
  }

  implicit val decodeDouble:JsonReader[Double] = pure {
    case JsDouble(d) => JsSuccess(d)
    case _ => JsError(new IllegalArgumentException("invalid input"))
  }

  implicit def decodeHList[H,T <: HList](implicit hReader: JsonReader[H], tReader:JsonReader[T]): JsonReader[H::T] = pure {
    case JsObject(List(h,t @ _*)) => for {
         a <- hReader.decode(h._2)
         b <- tReader.decode(JsObject(t.toList))
    } yield a::b
    case _ => JsError(new IllegalArgumentException("bad input"))
  }

  implicit val decodeHNil:JsonReader[HNil] = pure[HNil] {
    case JsObject(List())  => JsSuccess(HNil)
    case _ => JsError(new IllegalArgumentException("bad inputs"))
  }
}

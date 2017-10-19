package com.marcom.flatten

import shapeless._
import ops.tuple.FlatMapper


trait LowPriorityFlatten extends Poly1 {
  implicit def default[T] = at[T](Tuple1(_))
}
object flatten extends LowPriorityFlatten {
  implicit def caseTuple[P <: Product](implicit lfm: Lazy[FlatMapper[P, flatten.type]]) =
    at[P](lfm.value(_))
}


object FlattenInterface {
  import ConversionSyntax._
  import Conversion._

}

trait Conversion[A,B]{
   def convert(a:A):B
}

object ConversionSyntax {
   implicit class ConversionOps[A](a:A) {
      def convertTo[B](implicit ev:Conversion[A,B]) = ev.convert(a)
   }
}

object Conversion {

  implicit def conv[A,B,R<:HList](implicit gena:Generic.Aux[A,R], genb:Generic.Aux[B,R] ): Conversion[A,B] = new Conversion[A,B]{
    override def convert(a: A): B = genb.from(gena.to(a))
  }

}


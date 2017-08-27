package com.marcom.serialization.csv

import shapeless.{Generic, Lazy, HNil, HList, ::}


trait CsvDecoder[A] {

  def decode(l:List[String]):(A,Int)

}

object CsvDecoder {

  def apply[A](implicit dec:CsvDecoder[A]): CsvDecoder[A] = dec

  def pure[A](f:List[String] => (A,Int)):CsvDecoder[A]  = new CsvDecoder[A] {
    override def decode(l: List[String]): (A,Int) = f(l)
  }

  implicit val intDecoder:CsvDecoder[Int] = pure{ l =>
    (l.head.toInt,1)
  }

  implicit val stringDecoder:CsvDecoder[String] = pure{ l =>
    (l.head,1)
  }

  implicit def hnilDecoder:CsvDecoder[HNil] = pure(l => (HNil,0))

  implicit def hlistDecoder[H,T<:HList](
                                implicit hd: CsvDecoder[H], td: CsvDecoder[T]) : CsvDecoder[H::T] = pure{ l =>
     val (h,i) = hd.decode(l)
     val (t,j) = td.decode(l.drop(i))
    (h::t,i + j)
  }

  implicit def decoder[A,R](
                    implicit gen: Generic.Aux[A,R],
                    decoder: Lazy[CsvDecoder[R]] ): CsvDecoder[A] = pure{ l =>
    val (a,i) = decoder.value.decode(l)
    (gen.from(a),i)
  }
}

package com.marcom.serialization.csv

import com.marcom.serialization.csv.CsvDecoder.{ParseErrors, ParseResult}
import shapeless.{::, Generic, HList, HNil, Lazy}

trait CsvDecoder[A] {

  def decode(l: List[String]): Either[ParseErrors, (A, Int)]

}

object CsvDecoder {

  type ParseErrors = List[String]

  type ParseResult[A] = Either[ParseErrors, (A, Int)]

  def apply[A](implicit dec: CsvDecoder[A]): CsvDecoder[A] = dec

  def pure[A](f: List[String] => ParseResult[A]): CsvDecoder[A] = new CsvDecoder[A] {
    override def decode(l: List[String]): ParseResult[A] = f(l)
  }

  implicit val intDecoder: CsvDecoder[Int] = pure[Int] { l =>
    try {
      Right((l.head.toInt, 1))
    }
    catch {
      case e: Exception => Left(List(e.getMessage))
    }
  }

  implicit val stringDecoder: CsvDecoder[String] = pure { l =>
    Right((l.head, 1))
  }

  implicit def hnilDecoder: CsvDecoder[HNil] = pure[HNil](l => Right((HNil, 0)))

  implicit def hlistDecoder[H, T <: HList](
                                            implicit hd: CsvDecoder[H], td: CsvDecoder[T]): CsvDecoder[H :: T] = new CsvDecoder[::[H, T]] {
    override def decode(l: List[String]): ParseResult[H::T] = {
      hd.decode(l).flatMap{ t =>
        td.decode(l.drop(t._2)).map{ t2 =>
          (t._1 :: t2._1, t._2 + t2._2)
        }
      }
    }
  }

  implicit def decoder[A, R](
                              implicit gen: Generic.Aux[A, R],
                              decoder: Lazy[CsvDecoder[R]]): CsvDecoder[A] = pure { l =>
    decoder.value.decode(l).map{ t =>
      (gen.from(t._1), t._2)
    }
  }
}

object Csv {
  def decode[A](l:List[String])(implicit ev:CsvDecoder[A]): ParseResult[A] = ev.decode(l)
}

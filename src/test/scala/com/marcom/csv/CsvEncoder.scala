package com.marcom.csv

import shapeless.{::, CNil,:+:,Coproduct,Inl,Inr, Generic, HList, HNil, Lazy}


trait CsvEncoder[A] {

  def encode(a:A): List[String]

}

object CsvEncoder {

  def apply[A](implicit enc: CsvEncoder[A]) = enc

  def pure[A](f: A=>List[String]): CsvEncoder[A] = new CsvEncoder[A] {
    override def encode(a: A): List[String] = f(a)
  }

  implicit val hNilEnc:CsvEncoder[HNil] = pure(_ => List())

  implicit def hListEncoder[H,T<:HList](implicit headEnc:Lazy[CsvEncoder[H]], tailEnc:CsvEncoder[T]): CsvEncoder[H::T] = pure{
    case h::t => headEnc.value.encode(h) ++ tailEnc.encode(t)
  }

  implicit def cNilEnc:CsvEncoder[CNil] = pure(_ => List())

  implicit def coProductEnc[H,T <: Coproduct](
                                               implicit hEncoder:Lazy[CsvEncoder[H]],
                                                        tEncoder:CsvEncoder[T]):CsvEncoder[H :+: T] = pure {
    case Inl(h) => hEncoder.value.encode(h)
    case Inr(t) => tEncoder.encode(t)
  }

  implicit val stringEnc:CsvEncoder[String] = pure(s => List(s))

  implicit val intEnc:CsvEncoder[Int] = pure(i => List(i.toString))

  implicit val booleanEnc:CsvEncoder[Boolean] = pure(b => List(if(b) "true" else "false" ))

  implicit def csvEncoder[A,R](
                                implicit gen: Generic.Aux[A,R],
                                enc:Lazy[CsvEncoder[R]] ):CsvEncoder[A] = pure(a => enc.value.encode(gen.to(a)))
}

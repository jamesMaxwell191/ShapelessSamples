package com.marcom.ops.list

import shapeless.{::,HList,HNil}


trait Last[L<:HList] {

  type Out

  def apply(l:L):Out

}

object Last {

  def apply[L<:HList](implicit ev: Last[L]):Aux[L,ev.Out] = ev

  type Aux[L<:HList,O] = Last[L]{type Out = O}

  implicit def lastSingle[H]:Aux[H::HNil,H] = new Last[H::HNil]{
    override type Out = H

    override def apply(l: H::HNil): H = l.head
  }

  implicit def last[H,T<:HList](implicit ev:Last[T]):Aux[H::T,ev.Out] = new Last[H::T]{
    override type Out = ev.Out

    override def apply(l: H::T): ev.Out = ev(l.tail)
  }
}

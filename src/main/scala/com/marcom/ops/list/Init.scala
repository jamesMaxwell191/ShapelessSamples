package com.marcom.ops.list

import shapeless.{HList,HNil,::}


trait Init[L<:HList] {
   type Out<:HList

   def apply(l:L): Out

}

object Init {

  def apply[T<:HList](implicit ev: Init[T]): Aux[T,ev.Out] = ev

  type Aux[L<:HList,O] = Init[L]{type Out = O}

  implicit def finalInit[A,B]: Aux[A::B::HNil,A::HNil] = new Init[A::B::HNil]{
    type Out = A::HNil

    override def apply(l: A::B::HNil): A::HNil = l.head::HNil
  }
  implicit def init[A,T<:HList](implicit ev: Init[T]): Aux[A::T,A::ev.Out] = new Init[A::T]{
    type Out = A::ev.Out

    override def apply(l: A::T): A::ev.Out = l.head::ev(l.tail)
  }
}

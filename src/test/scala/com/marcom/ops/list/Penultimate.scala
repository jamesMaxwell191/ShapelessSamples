package com.marcom.ops.list

import shapeless.HList

/**
  * Created by douglasmaxwell on 07/08/2017.
  */
trait Penultimate[L<:HList] {

  type Out

  def apply(l:L): Out

}

object Penultimate {

   type Aux[L<:HList,O] = Penultimate[L]{type Out = O}

   def apply[L<:HList](implicit ev: Penultimate[L]): Aux[L,ev.Out] = ev

   implicit def penultimate[M<:HList,L<:HList] (implicit init: Init.Aux[L,M],last:Last[M]): Penultimate.Aux[L,last.Out] = new Penultimate[L] {
     type Out = last.Out

     override def apply(l: L): last.Out = last(init(l))
   }
}


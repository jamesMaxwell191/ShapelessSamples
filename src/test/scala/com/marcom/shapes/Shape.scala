package com.marcom.shapes

case class Point(x:Int,y:Int)

sealed trait Shape

case class Circle(centre:Point,radius:Int) extends Shape

case class Rectangle(leftTop:Point,bottomRight:Point)

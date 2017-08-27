package com.marcom.circe

sealed trait JsValue

case class JsString(value:String) extends JsValue

case class JsDouble(value:Double) extends JsValue

case class JsInteger(value:Int) extends JsValue

case class JsBoolean(value:Boolean) extends JsValue

case class JsArray(values:List[JsValue]) extends JsValue

case class JsObject(values:List[(String,JsValue)] = Nil) extends JsValue

case object JsNull extends JsValue
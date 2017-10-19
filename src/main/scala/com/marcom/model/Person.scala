package com.marcom.model

/**
  * Created by douglasmaxwell on 26/09/2017.
  */
case class Person(first:String,last:String,address:Address) {

}

case class Address(street:String,city:City)

case class City(name:String,code:String)
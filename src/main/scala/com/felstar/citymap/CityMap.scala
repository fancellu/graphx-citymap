package com.felstar.citymap

trait CityMap {
  def isJourneyPossible(startLocation:String,destinationLocation:String,onFoot:Boolean):Boolean
  type Link=Tuple2[String,String]
  type LinkSet=Set[Link]
  val isFootAllowed = """(.+)[\-|=](.+)""".r
  val isCarAllowed = "(.+)=(.+)".r
}
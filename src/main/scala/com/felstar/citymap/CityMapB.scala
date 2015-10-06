package com.felstar.citymap

import scala.collection.immutable.Queue
 
// 2nd solution, this time using a breadth-first algorithm, but still very functional, immutable, recursive. 
// Still passes unit tests, as it should
 
class CityMapB(routesAndLocations:String ) extends CityMap{

 private val groupings = routesAndLocations.split(",")   
 private lazy val footLinks:LinkSet = groupings.collect{case isFootAllowed(a, b) => a->b}.toSet                                                 
 private lazy val roadLinks:LinkSet = groupings.collect{case isCarAllowed(a, b) => a->b}.toSet
 
 type Edges=Map[String, Set[String]]
 def toEdges(linkSet:LinkSet):Edges=linkSet.groupBy(_._1).mapValues(_.map(_._2))
 lazy val footEdges:Edges=toEdges(footLinks)
 lazy val roadEdges:Edges=toEdges(roadLinks)
  
  def isJourneyPossible(startLocation:String,destinationLocation:String,onFoot:Boolean):Boolean={
  
    @scala.annotation.tailrec
    def search(queue:Queue[String],seen:Set[String]):Boolean={
      if (queue.isEmpty)  return false      
      val currentLocation = queue.head
      if (currentLocation==destinationLocation) return true        
      val destinations = (if (onFoot) footEdges else roadEdges).getOrElse(currentLocation,Set.empty)
      val todo=destinations--seen
      search(queue.tail++todo,seen++todo)        
    }    
    
    search(Queue(startLocation),Set(startLocation))
   }
}
package com.felstar.citymap

// 1st solution

class CityMapA(routesAndLocations: String) extends CityMap{

 @scala.annotation.tailrec
 private final def getValidJourneys(linkSet: LinkSet): LinkSet = {
   // find new nodes that can be connected based on the nodes we already have
    // e.g. if set contains (a->b, b->c), produce new link (a->c)
    val foundRelatedNodes = for {
      (a, b) <- linkSet
      (c, d) <- linkSet
      if c == b
    } yield a->d
    
    // then combine that set with the original set of nodes
    val combined=foundRelatedNodes | linkSet 
    if (combined==linkSet) linkSet// if no change then return
     else getValidJourneys(combined) // else recurse    
  }
                         
  // We create 2 inputs, for each transport type, one for foot user, one for car user
 private val groupings = routesAndLocations.split(",")                
 private lazy val footLinks:LinkSet = groupings.collect{case isFootAllowed(a, b) => a->b}.toSet                                                 
 private lazy val roadLinks:LinkSet = groupings.collect{case isCarAllowed(a, b) => a->b}.toSet
  // We then pre-compute all valid journeys for each transport type
 private lazy val footValid=getValidJourneys(footLinks)
 private lazy val roadValid=getValidJourneys(roadLinks)
  // This takes more memory than a breadth-first search, but leads to much less work below
  // On a high traffic service this should lead to large cpu savings, assuming route graph doesn't change all the time
  // Even a large valid linkset can be scaled out to Memory Grid or Database
  // Of course on a very large graph other solutions may well work better. I'm not trying to take on Google Maps
  // Chosen for elegance, readability, immutability, functional Scala idioms.
 def isJourneyPossible(startLocation:String,destinationLocation:String,onFoot:Boolean):Boolean={
   (if (onFoot) footValid else roadValid) contains (startLocation->destinationLocation)
 }
}
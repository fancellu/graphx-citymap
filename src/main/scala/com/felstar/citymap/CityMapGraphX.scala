package com.felstar.citymap

//import scala.collection.immutable.Queue
 
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.graphx._
import org.apache.spark.rdd.RDD
import org.apache.spark.graphx.lib.ShortestPaths
import org.apache.log4j.Logger
import org.apache.log4j.Level


// 3rd solution, this time using GraphX+Spark 
// Not very fast, but is really for scalable solutions and not toy data volumes on one box
 
object CityMapGraphX{
     Logger.getLogger("org").setLevel(Level.OFF)
     Logger.getLogger("akka").setLevel(Level.OFF)		
      
        val conf = new SparkConf()
          .setAppName("CityMapGraphX")
          .setMaster("local")
          .set("spark.driver.memory", "1G")
        val sc = new SparkContext(conf)
}

class CityMapGraphX(routesAndLocations:String ) extends CityMap{
  
     val groupings = routesAndLocations.split(",")
     val footLinks:LinkSet = groupings.collect{case isFootAllowed(a, b) => a->b}.toSet   
     val roadLinks:LinkSet = groupings.collect{case isCarAllowed(a, b) => a->b}.toSet
     
     val nodes=footLinks.flatMap(link=>Set(link._1,link._2))     
     val zipped=nodes.zip(Range.Long(0,nodes.size,1))
     val swapped=zipped.map(_.swap).toList
     val lookup=zipped.toMap
     
     lazy val nodesRDD=CityMapGraphX.sc.parallelize(swapped)
     
     lazy val relsFoot=CityMapGraphX.sc.parallelize(for ((src,dst)<-footLinks.toList)       
      yield Edge(lookup(src),lookup(dst),"foot"))
     
     lazy val relsRoad=CityMapGraphX.sc.parallelize(for ((src,dst)<-roadLinks.toList)       
      yield Edge(lookup(src),lookup(dst),"road"))
     
     lazy val footGraph = Graph(nodesRDD, relsFoot)
     lazy val roadGraph = Graph(nodesRDD, relsRoad)
     
       // we're being eager, we get all distances to all connected nodes here
     lazy val shortestFoot=ShortestPaths.run(footGraph, swapped.map(t=>t._1))
     lazy val shortestRoad=ShortestPaths.run(roadGraph, swapped.map(t=>t._1))
        
  def isJourneyPossible(startLocation:String,destinationLocation:String,onFoot:Boolean):Boolean={
    val dest=lookup.getOrElse(destinationLocation,return false)
    val src=lookup.getOrElse(startLocation,return false)
    
     // if being a bit lazier, would only compute 1 dest vertex, not all of them
     // would be nice if we could be lazier still, i.e. get distance from src->dest on demand
    //lazy val shortestFoot=org.apache.spark.graphx.lib.ShortestPaths.run(footGraph, Seq(dest))
    //lazy val shortestRoad=org.apache.spark.graphx.lib.ShortestPaths.run(roadGraph, Seq(dest))
    
    val shortest=if (onFoot) shortestFoot else shortestRoad    
    
    val (_,smap)=shortest.vertices.filter(_._1==src).first
        
    smap.contains(dest)
   }
}
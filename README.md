# CityMap coding test plus 3 solutions #

The problem is to provide an algorithm to calculate if a journey is possible for a series of locations in a city, connected by roads. You need to write an implementation for a class as follows:

    /**
     * @param routesAndLocations A String specification of the 
     * routes between locations. A comma-separated list
     * of roads and paths between locations defined as 
     * [startLocation][type][endLocation] e.g.
     *
     * "a-b" represents two locations, where travel is only possible 
     *   from a to b on foot 
     * "a-b,b-a" represents two locations, where travel 
     *   is possible in both directions on foot
     * "a=b,b=c,c=a" represents a triangular one-way road travelling 
     *   from a, through b and c, and finally back to a.
     *
     * Routes can be of two types:
     * - Paths (accessible on foot only)
     * = Roads (accessible on foot & by car)  
     */
    class CityMap(routesAndLocations: String) {
    
      /**
       * Determine whether a journey from the startLocation to the
       * destinationLocation can be made, based on the available routes
       * and whether on foot (onFoot=true), or by car (onFoot=false)
       */
      def isJourneyPossible(startLocation: String, destinationLocation: String, onFoot: Boolean): Boolean = ???
    
    }



## Solutions ##

3 solutions are supplied, first 2 in pure Scala, the 3rd using Spark+GraphX

[CityMapA](../../tree/master/src/main/scala/com/felstar/citymap/CityMapA.scala)

[CityMapB](../../tree/master/src/main/scala/com/felstar/citymap/CityMapB.scala)

[CityMapGraphX](../../tree/master/src/main/scala/com/felstar/citymap/CityMapGraphX.scala)

along with ScalaTest to test them all

[CityMapTest](../../tree/master/src/test/scala/com/felstar/citymap/CityMapTest.scala) 

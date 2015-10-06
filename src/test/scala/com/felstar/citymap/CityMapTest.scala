package com.felstar.citymap

import org.scalatest._

trait CityMapBehaviours extends Matchers{ this:FunSpec=>
  
  def test1(cmFactory: String=>CityMap){
    
    val cm=cmFactory("a-b,b-c,c-d,b-x,x-a,a=M1,M1=M2,M2-tiny")
    
    describe("CityMap") {
      it ("should handle a->d FOOT") {            
         cm.isJourneyPossible("a","d",true) shouldBe true             
      }
      it ("should handle a->b FOOT") {            
         cm.isJourneyPossible("a","b",true) shouldBe true             
      }
      it ("should handle b->a FOOT") {            
         cm.isJourneyPossible("b","a",true) shouldBe true             
      }
      it ("should handle a->z FOOT") {            
         cm.isJourneyPossible("a","z",true) shouldBe false             
      }
      it ("should handle c->x FOOT") {            
         cm.isJourneyPossible("c","x",true) shouldBe false             
      }
      it ("should handle x->M1 FOOT") {            
         cm.isJourneyPossible("x","M1",true) shouldBe true             
      }
      it ("should handle x->M1 CAR") {            
         cm.isJourneyPossible("x","M1",false) shouldBe false             
      }
      it ("should handle a->tiny FOOT") {            
         cm.isJourneyPossible("a","tiny",true) shouldBe true             
      }
      it ("should handle a->tiny CAR") {            
         cm.isJourneyPossible("a","tiny",false) shouldBe false             
      }
    }    
  }  
  
  def testEmpty(cm: String=>CityMap){    
    describe("Empty CityMap") {  
       it ("should handle a->d FOOT") {           
       cm("").isJourneyPossible("a","d",true) shouldBe false             
      }  
    }    
  }  
  def testNastyLoop(cm: String=>CityMap){    
    describe("Nasty Loop") {  
        it ("should handle a->zzz FOOT") {           
       cm("a-b,b-c,c-d,d-b,b-zzz").isJourneyPossible("a","zzz",true) shouldBe true             
      } 
    }    
  }  
}

class CityMapTest extends FunSpec with CityMapBehaviours{
      
   def test(cm: String=>CityMap)={     
    it should behave like test1(cm)
    it should behave like testEmpty(cm)
    it should behave like testNastyLoop(cm)
   }
     // shared tests with different fixtures, passing in factory function
   describe("with CityMapA"){
    test(new CityMapA(_))
   }
   describe("with CityMapB"){
    test(new CityMapB(_))
   }
   describe("with CityMapGraphX"){
    test(new CityMapGraphX(_))
   }      
}
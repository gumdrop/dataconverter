package org.chilternquizleague.util

import org.chilternquizleague.domain._
import java.util.HashMap
import scala.collection.JavaConversions._
import org.chilternquizleague.domain.BaseEntity
import com.fasterxml.jackson.databind.ObjectMapper
import java.util.{ List => JList }
import java.util.{ Map => JMap }
import java.util.ArrayList
import java.util.logging.Logger
import scala.collection.mutable._
import org.chilternquizleague.util.JacksonUtils._

object DBDumper {

  val dumpTypes: List[Class[_ <: BaseEntity]] = List(classOf[Venue], classOf[User], classOf[Team], classOf[CommonText], classOf[Fixtures], classOf[Results], classOf[Competition], classOf[Season], classOf[GlobalApplicationData])


  def load(entities: JMap[String, JList[JMap[String, Any]]]) = new DBDumper().load(entities)

}

private class DBDumper {
 
  def load(entitySet: JMap[String, JList[JMap[String, Any]]]) = {

    val realised = Map[String,ListBuffer[Any]]()
    
    for {
      t <- DBDumper.dumpTypes
      e <- entitySet.get(t.getName())
    } {
      if (String.valueOf(e.get("refClass")).contains("Competition")) {
        e.put("@class", "." + e.get("refClass"))
      }
      val s = mapper.writeValueAsString(e)

      realised += ((t.getName ,realised.getOrElse(t.getName,ListBuffer()) += mapper.readValue(s, t)))
    }
    
    cleanup(realised).asInstanceOf[Map[String, List[BaseEntity]]]
  }
  
  def cleanup(input:Map[String, ListBuffer[Any]]):Map[String, ListBuffer[Any]] = {
    
     cleanupResults(cleanupFixtures(cleanupCompetitions(input)))
  }
  
  def cleanupCompetitions(input:Map[String, ListBuffer[Any]]):Map[String, ListBuffer[Any]] = {
    
    val competitions = input(classOf[Competition].getName).asInstanceOf[ListBuffer[Competition]]
    val seasons = input(classOf[Season].getName).asInstanceOf[ListBuffer[Season]]
    
    val compIds = seasons.flatMap(_.competitions.values).map(_.id)
    
    val filtered = competitions.filter(c => compIds.contains(c.id.toString))
    
    input += ((classOf[Competition].getName, filtered.asInstanceOf[ListBuffer[Any]]))
    
    
  }
  
  def cleanupResults(input:Map[String, ListBuffer[Any]]):Map[String, ListBuffer[Any]] = {
    
    val results = input(classOf[Results].getName).asInstanceOf[ListBuffer[Results]]
    
    val competitions = input(classOf[Competition].getName).asInstanceOf[ListBuffer[Competition]]
    
    def fm(c:Competition):JList[Ref[Results]] = {

       c match{
         case a:TeamCompetition => a.results
         case _  => new ArrayList
       }
    
    }
    
    val resultIds = competitions.flatMap(fm _).map(_.id)
    
    val filtered = results.filter(r => resultIds.contains(r.id.toString))
    
    input += ((classOf[Results].getName, filtered.asInstanceOf[ListBuffer[Any]]))
  }
  
  def cleanupFixtures(input:Map[String, ListBuffer[Any]]):Map[String, ListBuffer[Any]] = {
    
    val fixtures = input(classOf[Fixtures].getName).asInstanceOf[ListBuffer[Fixtures]]
    
    val competitions = input(classOf[Competition].getName).asInstanceOf[ListBuffer[Competition]]
    
    def fm(c:Competition):JList[Ref[Fixtures]] = {

       c match{
         case a:TeamCompetition => a.fixtures
         case _  => new ArrayList
       }
    
    }
    
    val fixtureIds = competitions.flatMap(fm _).map(_.id)
    
    val filtered = fixtures.filter(r => fixtureIds.contains(r.id.toString))
    
    input += ((classOf[Fixtures].getName, filtered.asInstanceOf[ListBuffer[Any]]))
  }

}
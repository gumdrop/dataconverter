package org.chilternquizleague.domain


import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import java.util.{List => JList}
import java.util.{Map => JMap}
import java.util.ArrayList
import scala.beans.BeanProperty
import java.util.HashMap
import scala.collection.JavaConversions._
import java.util.Calendar
import org.chilternquizleague.domain.util.JacksonAnnotations._
import scala.collection.immutable.HashSet
import java.util.Date
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.annotation.JsonInclude

@JsonIgnoreProperties(value=Array("parent"), ignoreUnknown=true)
class BaseEntity{

  var id:java.lang.Long = null
  
  var retired:Boolean = false
  
}

case class Ref[T](id:String)


@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class User extends BaseEntity{
  

  var name:String = null

  var email:String  = null
  
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Venue extends BaseEntity{
  var name:String = null
  var address:String = null
  var postcode:String = null
  var website:String = null
  var phone:String = null
  var email:String = null
  var imageURL:String = null
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Team extends BaseEntity{

	var name:String = null
  var shortName:String = null
  var rubric:Text = new Text
	

	var venue:Ref[Venue] = null

	var users:JList[Ref[User]] = new ArrayList

}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class CommonText extends BaseEntity{
  
  var name:String = null

  var text:JMap[String,TextEntry] = new HashMap
  
  
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class GlobalApplicationData extends BaseEntity{

  var frontPageText:String = null
  var leagueName:String = null
  var senderEmail:String = null
  var cloudStoreBucket:String = null


  var currentSeason:Ref[Season] = null

  var globalText:Ref[CommonText] = null


  var emailAliases:JList[EmailAlias] = new ArrayList

}




@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Season extends BaseEntity{

  import Competition._

  var startYear:Int =0

  var endYear:Int = 0
  
  var competitions:JMap[CompetitionType, Ref[Competition]] = new HashMap
  
  var calendar:JList[CalendarEvent] = new ArrayList
  
}


@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Results extends BaseEntity{
  var date:Date = new Date
  var description:String = null
  var iconPath:String = null
  var results:JList[Result] = new ArrayList


}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Result{
	var homeScore:Int = 0
	var awayScore:Int = 0
	var fixture:Fixture = null
	var reports:JList[Report] = new ArrayList
  var note:String = null
  
}



@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Fixtures extends BaseEntity{
  var start:Date = null
	var end:Date = null
	var competitionType:CompetitionType = null
	var description:String = null
	var fixtures:JList[Fixture] = new ArrayList
	
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Event{

  var venue:Ref[Venue] = null
  var start:Date = null
  var end:Date = null
  
  def getVenue() = venue
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class CalendarEvent extends Event{
  var description:String = null
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Fixture extends Event{
	var home:Ref[Team] = null
	var away:Ref[Team] = null
}



@JsonAutoDetect(fieldVisibility=Visibility.ANY)
@JsonTypeInfo(use=JsonTypeInfo.Id.MINIMAL_CLASS, include=JsonTypeInfo.As.PROPERTY, property="@class")
abstract class Competition(
    var `type`:CompetitionType,
    var description:String,
    var startTime:String,
    var endTime:String,
    var iconPath:String,
    var subsidiary:Boolean = false
    ) extends BaseEntity{
	description = `type`.description
	var text:String = null

}


@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class IndividualCompetition extends SingletonCompetition(CompetitionType.INDIVIDUAL, "/images/icons/competition/individual.svg")


abstract class SingletonCompetition(`type`:CompetitionType,
    iconPath:String)  extends Competition(`type`,"","20:30","22:00", iconPath, false){

  var event:Event = new Event
}

abstract class TeamCompetition(
    `type`:CompetitionType,
    iconPath:String, 
    subsidiary:Boolean = false)  extends Competition(`type`,"","20:30","22:00", iconPath, subsidiary){
 
	var hasStats = false

  var fixtures:JList[Ref[Fixtures]] = new ArrayList

  var results:JList[Ref[Results]] = new ArrayList

  var subsidiaryCompetition:Ref[TeamCompetition] = null

}

abstract class BaseLeagueCompetition(
    `type`:CompetitionType,
    iconPath:String, 
    subsidiary:Boolean = false)  extends TeamCompetition(`type`, iconPath,subsidiary){
  
  	var win:Int = 2
	var loss:Int = 0;
	var draw:Int = 1;
	
	var leagueTables:JList[LeagueTable] = new ArrayList
	

}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class LeagueCompetition extends BaseLeagueCompetition(CompetitionType.LEAGUE, "/images/icons/competition/league.svg"){
 
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class BeerCompetition extends BaseLeagueCompetition(CompetitionType.BEER,"/images/icons/competition/beer.svg" ,true)

abstract class KnockoutCompetition(`type`:CompetitionType, iconPath:String) extends TeamCompetition(`type`, iconPath){
	 
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class BuzzerCompetition extends SingletonCompetition(CompetitionType.BUZZER,"/images/icons/competition/buzzer.svg")

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class CupCompetition extends KnockoutCompetition(CompetitionType.CUP, "/images/icons/competition/cup.svg")

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class PlateCompetition extends KnockoutCompetition(CompetitionType.PLATE,"/images/icons/competition/plate.svg")

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class EmailAlias{

  var alias:String = null
  var user:Ref[User]= null 
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class TextEntry(var name:String,var text:String){
  def this() = {this(null,null)}
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Text(var text:String){
  def this() = {this(null)}
 
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class Report{
  var text:Text = new Text
  var team:Ref[Team] = null

}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class LeagueTable{
  	
	var description:String = null
	var rows:JList[LeagueTableRow] = new ArrayList
	
}

@JsonAutoDetect(fieldVisibility=Visibility.ANY)
class LeagueTableRow{
  	var team:Ref[Team] = null
	var position:String = null
	var played = 0
	var won = 0
	var lost = 0
	var drawn = 0
	var leaguePoints = 0
	var matchPointsFor = 0
	var matchPointsAgainst = 0
  
}




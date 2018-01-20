package quizleague.domain

import java.util.Date
import java.time.LocalDate
import java.time.LocalTime
import java.time.Duration
import io.circe.generic._

import quizleague.util.json.codecs.ScalaTimeCodecs._
import quizleague.util.json.codecs.DomainCodecs._


@JsonCodec
sealed trait Competition extends Entity
{

  
  val name:String
  val text:Ref[Text]
  val icon:Option[String]
  override val retired = false
  
}

case class LeagueCompetition(
  id:String,
  name:String,
  startTime:LocalTime,
  duration:Duration,
  fixtures:List[Ref[Fixtures]],

  tables:List[Ref[LeagueTable]],
  text:Ref[Text],
  subsidiary:Option[Ref[Competition]],
  textName:String = "league-comp",
  icon:Option[String] = None
  
) extends Competition with MainLeagueCompetition


case class CupCompetition(
  id:String,
  name:String,
  startTime:LocalTime,
  duration:Duration,
  fixtures:List[Ref[Fixtures]],

  text:Ref[Text],
  textName:String,
  icon:Option[String] = None
) extends Competition with KnockoutCompetition

case class SubsidiaryLeagueCompetition(
  id:String,
  name:String,
  fixtures:List[Ref[Fixtures]],
  tables:List[Ref[LeagueTable]],
  text:Ref[Text],
  textName:String = "beer-comp",
  icon:Option[String] = None
) extends Competition with SubsidiaryCompetition  with CompetitionTables with FixturesCompetition


case class SingletonCompetition(
  id:String,
  name:String,
  event:Option[Event],
  textName:String,
  text:Ref[Text],
  icon:Option[String] = None
) extends Competition with BaseSingletonCompetition

object Competition



 trait BaseSingletonCompetition{
    
  val event:Option[Event]
  val textName:String

}

 trait ScheduledCompetition{

  val startTime:LocalTime
  val duration:Duration
}


 trait FixturesCompetition{
  val fixtures:List[Ref[Fixtures]]
}

 trait TeamCompetition extends FixturesCompetition{
     val textName:String
 }

 trait CompetitionTables{
    val tables:List[Ref[LeagueTable]]
}

 trait BaseLeagueCompetition extends TeamCompetition with ScheduledCompetition with CompetitionTables{
   val win = 2
   val draw = 1
   val loss = 0
 }

 trait MainLeagueCompetition extends BaseLeagueCompetition{
  val subsidiary:Option[Ref[Competition]]
}

 trait KnockoutCompetition extends TeamCompetition with ScheduledCompetition

 trait SubsidiaryCompetition{
     val textName:String
 }








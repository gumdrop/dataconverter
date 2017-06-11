package quizleague.domain

import java.util.Date
import java.time.LocalDate
import java.time.LocalTime
import java.time.Duration
import io.circe.generic.JsonCodec

import quizleague.util.json.codecs.ScalaTimeCodecs._
import quizleague.util.json.codecs.DomainCodecs._


@JsonCodec
sealed trait Competition extends Entity
{

  
  val name:String
  val text:Ref[Text]
  override val retired = false
  
}

case class LeagueCompetition(
  id:String,
  name:String,
  startTime:LocalTime,
  duration:Duration,
  fixtures:List[Ref[Fixtures]],
  results:List[Ref[Results]],
  tables:List[Ref[LeagueTable]],
  text:Ref[Text],
  subsidiary:Option[Ref[Competition]]
  
) extends Competition with MainLeagueCompetition


case class CupCompetition(
  id:String,
  name:String,
  startTime:LocalTime,
  duration:Duration,
  fixtures:List[Ref[Fixtures]],
  results:List[Ref[Results]],
  text:Ref[Text]
) extends Competition with KnockoutCompetition

case class SubsidiaryLeagueCompetition(
  id:String,
  name:String,
  results:List[Ref[Results]], 
  tables:List[Ref[LeagueTable]],
  text:Ref[Text]
) extends Competition with SubsidiaryCompetition with ResultsCompetition with CompetitionTables

case class SingletonCompetition(
  id:String,
  name:String,
  event:Option[Event],
  textName:String,
  text:Ref[Text]    
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

sealed trait ResultsCompetition{
   val results:List[Ref[Results]]
}

 trait FixturesCompetition{
  this:ResultsCompetition =>
  val fixtures:List[Ref[Fixtures]]
}

 trait TeamCompetition extends FixturesCompetition with ResultsCompetition

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

 trait SubsidiaryCompetition








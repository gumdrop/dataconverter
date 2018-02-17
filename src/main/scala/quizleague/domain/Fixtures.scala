package quizleague.domain

import java.time.LocalTime
import java.time.Duration
import java.time.LocalDate


case class Fixtures(
    id:String, 
    description:String,
    parentDescription:String,
    date:LocalDate,
    start:LocalTime,
    duration:Duration,
    fixtures:List[Ref[Fixture]],
    retired:Boolean = false) extends Entity
    
case class Fixture(
  id:String,
  description:String,
  parentDescription:String,
  venue: Ref[Venue],
  home: Ref[Team],
  away:Ref[Team],
  date: LocalDate,
  time: LocalTime,
  duration : Duration,
  result : Option[Result],
  subsidiary:Boolean = false,
  retired:Boolean = false
) extends BaseEvent with Entity

case class Result(
    homeScore:Int,
    awayScore:Int,
    submitter:Option[Ref[User]],
    note:Option[String],
    reports:Option[Ref[Reports]]
   )
    
case class Reports(
    id:String,
    reports:List[Report],
    retired:Boolean = false) extends Entity

case class Report(
    team:Ref[Team],
    text:Ref[Text])
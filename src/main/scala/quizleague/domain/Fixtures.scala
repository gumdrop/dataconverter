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
  retired:Boolean = false
) extends BaseEvent with Entity
package quizleague.domain

import java.time.LocalTime
import java.time.LocalDate
import java.time.Duration

trait BaseEvent {
  val venue: Ref[Venue]
  val date: LocalDate
  val time: LocalTime
  val duration :Duration
}

case class Event(
  venue: Ref[Venue],
  date: LocalDate,
  time: LocalTime,
  duration : Duration) extends BaseEvent

case class CalendarEvent(
  venue: Ref[Venue],
  date: LocalDate,
  time: LocalTime,
  duration : Duration,
  description: String) extends BaseEvent
    


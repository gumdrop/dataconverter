package dataconverter

import scala.language.implicitConversions
import java.util.UUID
import java.lang.{Long => JLong}
import java.time.Year
import java.util.Date
import java.time.LocalDate
import java.time.ZoneId
import java.time.LocalTime
import java.time.Duration
import scala.reflect.ClassTag

package object mappers {
  
  implicit def idToString(id:JLong):String = id.toString
  
  implicit def uuidToString(uuid:UUID):String = uuid.toString
  
  implicit def intToYear(in:Int) = Year.of(in)
  
  implicit def dateToLocalDate(date:Date):LocalDate = date.toInstant().atZone(ZoneId.systemDefault).toLocalDate
  
  implicit def dateToLocalTime(date:Date):LocalTime = date.toInstant().atZone(ZoneId.systemDefault).toLocalTime

  def duration(start:Date, end:Date) = Duration.ofMillis(end.getTime - start.getTime)

  def duration(start:LocalTime, end:LocalTime) = Duration.between(start, end)
  
  implicit def tagToName(tag:ClassTag[_]):String = tag.runtimeClass.getSimpleName.toLowerCase

  implicit def stringToLocalTime(s:String) = LocalTime.parse(s)
}
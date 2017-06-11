package dataconverter.mappers

import org.chilternquizleague.domain.{ Venue => CVenue }
import org.chilternquizleague.domain.{ User => CUser }
import org.chilternquizleague.domain.{ Team => CTeam }
import org.chilternquizleague.domain.{ Season => CSeason }
import org.chilternquizleague.domain.{ Competition => CCompetition }
import org.chilternquizleague.domain.{ TeamCompetition => CTeamCompetition }
import org.chilternquizleague.domain.{ Fixtures => CFixtures }
import org.chilternquizleague.domain.{ Fixture => CFixture }
import org.chilternquizleague.domain.{ Results => CResults }
import org.chilternquizleague.domain.{ Report => CReport }
import org.chilternquizleague.domain.{ LeagueTable => CLeagueTable }
import org.chilternquizleague.domain.{ LeagueTableRow => CLeagueTableRow}
import org.chilternquizleague.domain.{ Ref => CRef }
import org.chilternquizleague.util.JacksonUtils._
import scala.reflect.ClassTag
import java.io.Reader
import java.io.File
import java.io.FileReader
import java.util.{Map => JMap, List => JList}
import org.chilternquizleague.util.DBDumper

object CQLLoader {
  
  def load(fileName:String) = DBDumper.load(readFile(new File(fileName))).toMap
  
  def readObject[T:ClassTag](reader:Reader):T = mapper.read[T](reader).asInstanceOf[T]

  def readFile(file:File) = readObject[JMap[String,JList[JMap[String,Any]]]](new FileReader(file))
  
   
}
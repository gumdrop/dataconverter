package dataconverter.mappers

import Mappers._
import org.chilternquizleague.domain.{ BaseEntity => CEntity }
import quizleague.domain.container.DomainContainer
import java.util.{ Map => JMap, List => JList }
import org.chilternquizleague.util.DBDumper
import scala.reflect.ClassTag
import quizleague.domain.GlobalText
import scala.collection.mutable.ListBuffer

object Converter {

  def convert(input: Map[String, List[CEntity]]) = {

    implicit val cc = new ConversionContext(input)
    
    doConversion(input)

    import quizleague.domain._
    DomainContainer(
      //cc.get[ApplicationContext],
      cc.get[Competition],
      cc.get[Fixtures],
      cc.get[Fixture],
      cc.get[GlobalText],
      cc.get[LeagueTable],
      cc.get[Results].filter(_.fixtures != null),
      cc.get[Result],
      cc.get[Season],
      cc.get[Team],
      cc.get[Text],
      cc.get[User],
      cc.get[Venue])
  }

  def doConversion(input: Map[String, List[CEntity]])(implicit cc: ConversionContext) {
    import org.chilternquizleague.domain._

    def get[T <: CEntity](implicit tag: ClassTag[T]) = input(tag.runtimeClass.getName).asInstanceOf[ListBuffer[T]].toList

    get[Venue].foreach(map _)
    get[User].foreach(map _)
    get[Team].foreach(map _)
    get[Fixtures].foreach(map _)
    get[Results].foreach(map _)
    get[Competition].foreach(map(get[Results]) _)
    get[Season].foreach(map _)
    //get[GlobalApplicationData].foreach(map _)
    get[CommonText].foreach(map _)


  }

}
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
import quizleague.domain._
import java.util.UUID.{ randomUUID => uuid }
import scala.collection.JavaConversions._
import scala.reflect.ClassTag
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Duration
import java.util.Date
import quizleague.domain.LeagueCompetition
import org.chilternquizleague.domain.CommonText
import org.chilternquizleague.domain.GlobalApplicationData
import quizleague.domain.ApplicationContext

object Mappers {

  implicit def ref[T <: Entity](in: CRef[_])(implicit tag: ClassTag[T]): Ref[T] = if(in == null) null else Ref[T](tag, in.id)
  implicit def eref[T <: Entity](in: T)(implicit tag: ClassTag[T]): Ref[T] = Ref[T](tag, in.id)

  def map(in: CVenue)(implicit cc: ConversionContext): Venue = {
    import in._
    cc add Venue(id, name, address, Option(phone), Option(email), Option(website), Option(imageURL), retired)
  }

  def map(in: CUser)(implicit cc: ConversionContext): User = {
    import in._
    cc add User(id, name, email, retired)
  }

  def map(in: CTeam)(implicit cc: ConversionContext): Team = {
    import in._
    val text = cc add Text(uuid, if(rubric.text == null) "" else rubric.text, "text/html")
    cc add Team(id, name, shortName, venue, text, users.map(ref[User] _).toList, retired)
  }

  def map(in: CSeason)(implicit cc: ConversionContext): Season = {
    import in._
    val text = cc add Text(uuid, "", "text/html")
    cc add Season(
      id,
      startYear,
      endYear,
      text,
      competitions.values.map(ref[Competition] _).toList,
      calendar.map(e => CalendarEvent(ref(e.venue), e.start, e.start, duration(e.start, e.end), e.description)).toList)
  }

  def map(in: CFixtures)(implicit cc: ConversionContext): Fixtures = {
    import in._

    val fixs = cc add fixtures.map(f => Fixture(uuid, description, competitionType.toString, f.venue, f.home, f.away, f.start, f.start, duration(f.start, f.end))).toList

    cc add Fixtures(id, description, "", start, start, duration(start, end), fixs.map(eref[Fixture] _))

  }

  /**
   * must be run <em>after</em> map(CFixtures)
   */
  def map(in: CResults)(implicit cc: ConversionContext): Results = {
    import in._

    def map(r: CReport): Report = Report(r.team, eref(cc.add(Text(uuid, if(r.text.text == null) "" else r.text.text, "text/plain"))))

    def find(f: CFixture): Fixture = cc.get[Fixture].filter(p => p.date == dateToLocalDate(f.start) && p.home.id == f.home.id).head

    val res = cc add results.map(r => Result(uuid, find(r.fixture), r.homeScore, r.awayScore, None, if(r.note == null) "" else r.note, r.reports.filter(r => r.text.text != null && !r.text.text.isEmpty).map(map _).toList)).toList

    cc add Results(id, null, res.map(eref[Result] _))
  }
  
  def map(in:CLeagueTable)(implicit cc: ConversionContext) = {
    import in._
    
    def map(row:CLeagueTableRow) = {
      import row._
      LeagueTableRow(team, if(position == null) "" else position, played, won, lost, drawn, leaguePoints, matchPointsFor, matchPointsAgainst)
    }
    
    cc add LeagueTable(uuid, if(description == null) "" else description, rows.map(map _).toList)
    
  }

  def map(cres: List[CResults])(in: CCompetition)(implicit cc: ConversionContext): Competition = {

    import org.chilternquizleague.domain
    import domain.{ LeagueCompetition => CLeagueCompetition }
    import domain.{ BeerCompetition => CBeerCompetition }
    import domain.{ KnockoutCompetition => CKnockoutCompetition }
    import domain.{ IndividualCompetition => CIndividualCompetition }
    import domain.{ BuzzerCompetition => CBuzzerCompetition }
    
    def remapResults(res: List[CResults], comp: CTeamCompetition)(inref: Ref[Results])(implicit cc: ConversionContext) = {

      val in = cc.get[Results](inref.id)
      import in._

      val cres = res.filter(_.id.toString == in.id).head
      def find(): Fixtures = {
        cc.get[Fixtures].filter(i => comp.fixtures.exists(_.id == i.id)).filter(_.date == dateToLocalDate(cres.date)).head
      }

      cc replace Results(id, find(), results)

    }

    val c: Competition = in match {
      case a: CLeagueCompetition => {
        import a._

        val r = results.map(ref[Results] _).toList
        r.foreach(remapResults(cres, a) _)
        LeagueCompetition(id, description, startTime, duration(startTime, endTime), fixtures.map(ref[Fixtures] _).toList, r, leagueTables.map(t => eref(map(t))).toList, eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))), Option(subsidiaryCompetition))
        
      }
      case a: CBeerCompetition => {
        import a._

        
        def parentPred(c:CCompetition):Boolean = {
          c match {
          case d:CLeagueCompetition => {
            if (d.subsidiaryCompetition != null) d.subsidiaryCompetition.id.toString == a.id else false
          }
          case _ => false}
        }
        
//        val parent = cc.getInput[CCompetition](parentPred _).asInstanceOf[Option[CLeagueCompetition]]
//        
//        val r = results.map(ref[Results] _).toList
//         
//        r.foreach(remapResults(cres, parent.get) _)
//        
        results.foreach(r => cc remove[Results](r.id))
        
        SubsidiaryLeagueCompetition(id, description, List(), leagueTables.map(t => eref(map(t))).toList, eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))))

      }
      
      case a: CKnockoutCompetition => {
        import a._

        val r = results.map(ref[Results] _).toList
        r.foreach(remapResults(cres, a) _)
        CupCompetition(id, description, startTime, duration(startTime, endTime), fixtures.map(ref[Fixtures] _).toList, r, eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))))

      }
      
      case a: CIndividualCompetition => {
        import a._

        SingletonCompetition(id, "Individual", if(event.venue == null) None else Some(Event(event.venue, event.start, event.start, duration(event.start, event.end))),"individuals_front_page" ,eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))))

      }
      
      case a: CBuzzerCompetition => {
        import a._

        SingletonCompetition(
            id, 
            "Buzzer Quiz", 
            if(event.venue == null) None else Some(Event(event.venue, event.start, event.start, duration(event.start, event.end))),
            "buzzer_front_page" ,eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))))

      }
    }
    cc add c
  }
  
  def map(in:CommonText)(implicit cc:ConversionContext) = {
    import in._
    cc add GlobalText(id, name, text.map({case(n,t) => ((n,eref(cc.add(Text(uuid, if(t.text == null) "" else t.text, "text/html")))))}).toMap)
  }
  
  def map(in:GlobalApplicationData)(cc:ConversionContext) = {
    import in._
    println(s"here $in")
    cc add ApplicationContext(id,leagueName,globalText,currentSeason,senderEmail,emailAliases.map(a => EmailAlias(a.alias,a.user)).toList,cloudStoreBucket)
  }
}


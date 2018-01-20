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
import org.chilternquizleague.domain.{ Result => CResult }
import org.chilternquizleague.domain.{ Report => CReport }
import org.chilternquizleague.domain.{ LeagueTable => CLeagueTable }
import org.chilternquizleague.domain.{ LeagueTableRow => CLeagueTableRow}
import org.chilternquizleague.domain.{ Ref => CRef }
import quizleague.domain._
import java.util.UUID.{ randomUUID => uuid }
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
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
  implicit def erefOption[T <: Entity](in: Option[T])(implicit tag: ClassTag[T]): Option[Ref[T]] = in.map(x => Ref[T](tag, x.id))

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

  def map(results: List[CResults])(in: CFixtures)(implicit cc: ConversionContext): Fixtures = {
    import in._

    def find(f: CFixture) = {
      
      def map(r: CReport) = if(r.text.text == null || r.text.text.isEmpty) List.empty else List(Report(r.team, eref(cc.add(Text(uuid, r.text.text, "text/plain")))))
      
      def reports(reports:List[CReport]) = if(reportsEmpty(reports)) None else Some(eref(cc add Reports(uuid,reports.flatMap(map _))))
      
      def reportsEmpty(reports:List[CReport]) = reports.isEmpty || reports.forall(r => r.text == null || r.text.text == null || r.text.text.isEmpty())
      
      val r = results.filter(p => dateToLocalDate(p.date) == dateToLocalDate(f.start) && !p.description.toLowerCase().contains("beer")).flatMap(_.results.filter(_.fixture.home.id == f.home.id)).headOption
      
      r.map(r => Result(r.homeScore, r.awayScore, None, Option(r.note), reports(r.reports.toList)))
    }
    


    
    val fixs = cc add fixtures.map(f => Fixture(uuid, description, "", f.venue, f.home, f.away, f.start, f.start, duration(f.start, f.end), find(f))).toList

    cc add Fixtures(id, description, "", start, start, duration(start, end), fixs.map(eref[Fixture] _))

  }


  def map(in:CLeagueTable)(implicit cc: ConversionContext) = {
    import in._
    
    def map(row:CLeagueTableRow) = {
      import row._
      LeagueTableRow(team, if(position == null) "" else position, played, won, lost, drawn, leaguePoints, matchPointsFor, matchPointsAgainst)
    }
    
    cc add LeagueTable(uuid, if(description == null) "" else description, rows.map(map _).toList)
    
  }

  def map(in: CCompetition)(implicit cc: ConversionContext): Competition = {

    import org.chilternquizleague.domain
    import domain.{ LeagueCompetition => CLeagueCompetition }
    import domain.{ BeerCompetition => CBeerCompetition }
    import domain.{ KnockoutCompetition => CKnockoutCompetition }
    import domain.{CupCompetition => CCupCompetition, PlateCompetition => CPlateCompetition}
    import domain.{ IndividualCompetition => CIndividualCompetition }
    import domain.{ BuzzerCompetition => CBuzzerCompetition }
    

    val c: Competition = in match {
      case a: CLeagueCompetition => {
        import a._

        LeagueCompetition(id, description, startTime, duration(startTime, endTime), fixtures.map(ref[Fixtures] _).toList, leagueTables.map(t => eref(map(t))).toList, eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))), Option(subsidiaryCompetition),icon = Some("mdi-table-large"))
        
      }
      case a: CBeerCompetition => {
        import a._

        def makeFix(result:CResult) = {
          eref(cc add Fixture(uuid, description,"", result.fixture.getVenue, result.fixture.home, result.fixture.away, result.fixture.start, result.fixture.end, duration(result.fixture.start,result.fixture.end),Some(Result(result.homeScore,result.awayScore,None,None,None))))
        }
        
        val r = results.asScala
        
        val fixtures = 
        
        for(
          res <- r
          
        )yield{
            val rr = cc.getInput[CResults](_.id.toString == res.id).get
            cc add Fixtures(rr.id, description,"", rr.date,rr.date,duration(rr.date,rr.date), rr.results.map(makeFix _).toList)
        
        }

        
        SubsidiaryLeagueCompetition(id, description, fixtures.map(eref _).toList,leagueTables.map(t => eref(map(t))).toList, eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))),icon = Some("mdi-glass-mug"))

      }
      
      case a: CKnockoutCompetition => {
        import a._
        val textName = a match{
          case c:CCupCompetition => "cup-comp"
          case c:CPlateCompetition => "plate-comp"
        }
        
        val iconName = a match{
          case c:CCupCompetition => "mdi-coffee-outline"
          case c:CPlateCompetition => "mdi-octagon-outline"
        }

        CupCompetition(id, description, startTime, duration(startTime, endTime), fixtures.map(ref[Fixtures] _).toList, eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))),textName, Some(iconName))

      }
      
      case a: CIndividualCompetition => {
        import a._

        SingletonCompetition(id, "Individual", if(event.venue == null) None else Some(Event(event.venue, event.start, event.start, duration(event.start, event.end))),"individuals_front_page" ,eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))),Some("mdi-account-outline"))

      }
      
      case a: CBuzzerCompetition => {
        import a._

        SingletonCompetition(
            id, 
            "Buzzer Quiz", 
            if(event.venue == null) None else Some(Event(event.venue, event.start, event.start, duration(event.start, event.end))),
            "buzzer_front_page" ,eref(cc.add(Text(uuid, if(text == null) "" else text, "text/html"))),Some("mdi-bell-ring-outline"))

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


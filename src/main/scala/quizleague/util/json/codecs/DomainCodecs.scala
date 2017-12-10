package quizleague.util.json.codecs

import quizleague.domain._
import ScalaTimeCodecs._
import quizleague.domain.container.DomainContainer

object DomainCodecs{
  import io.circe._, io.circe.generic.semiauto._,io.circe.generic.auto._
  implicit val competitionRefDecoder: Decoder[Ref[Competition]] = deriveDecoder
  implicit val competitionRefEncoder: Encoder[Ref[Competition]] = deriveEncoder
  implicit val fixturesRefDecoder: Decoder[Ref[Fixtures]] = deriveDecoder
  implicit val fixturesRefEncoder: Encoder[Ref[Fixtures]] = deriveEncoder
  implicit val leagueTableRefDecoder: Decoder[Ref[LeagueTable]] = deriveDecoder
  implicit val leagueTableRefEncoder: Encoder[Ref[LeagueTable]] = deriveEncoder
  implicit val textRefDecoder: Decoder[Ref[Text]] = deriveDecoder
  implicit val textRefEncoder: Encoder[Ref[Text]] = deriveEncoder
  implicit val eventDecoder: Decoder[Event] = deriveDecoder
  implicit val eventEncoder: Encoder[Event] = deriveEncoder
  
  implicit val venueDecoder: Decoder[Venue] = deriveDecoder
  implicit val venueEncoder: Encoder[Venue] = deriveEncoder
  implicit val teamDecoder: Decoder[Team] = deriveDecoder
  implicit val teamEncoder: Encoder[Team] = deriveEncoder
  implicit val fixturesDecoder: Decoder[Fixtures] = deriveDecoder
  implicit val fixturesEncoder: Encoder[Fixtures] = deriveEncoder
  implicit val fixtureDecoder: Decoder[Fixture] = deriveDecoder
  implicit val fixtureEncoder: Encoder[Fixture] = deriveEncoder
  implicit val resultDecoder: Decoder[Result] = deriveDecoder
  implicit val resultEncoder: Encoder[Result] = deriveEncoder
  implicit val reportsDecoder: Decoder[Reports] = deriveDecoder
  implicit val reportsEncoder: Encoder[Reports] = deriveEncoder
  implicit val competitionDecoder: Decoder[Competition] = deriveDecoder
  implicit val competitionEncoder: Encoder[Competition] = deriveEncoder
  implicit val leagueTableDecoder: Decoder[LeagueTable] = deriveDecoder
  implicit val leagueTableEncoder: Encoder[LeagueTable] = deriveEncoder
  implicit val applicationContextDecoder: Decoder[ApplicationContext] = deriveDecoder
  implicit val applicationContextEncoder: Encoder[ApplicationContext] = deriveEncoder
  implicit val globalTextDecoder: Decoder[GlobalText] = deriveDecoder
  implicit val globalTextEncoder: Encoder[GlobalText] = deriveEncoder
  implicit val seasonDecoder: Decoder[Season] = deriveDecoder
  implicit val seasonEncoder: Encoder[Season] = deriveEncoder
  implicit val textDecoder: Decoder[Text] = deriveDecoder
  implicit val textEncoder: Encoder[Text] = deriveEncoder
  implicit val userDecoder: Decoder[User] = deriveDecoder
  implicit val userEncoder: Encoder[User] = deriveEncoder
  implicit val containerDecoder: Decoder[DomainContainer] = deriveDecoder
  implicit val containerEncoder: Encoder[DomainContainer] = deriveEncoder
  
}
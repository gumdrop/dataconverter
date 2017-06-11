package quizleague.util.json.codecs

import io.circe._
import cats.syntax.either._
import java.time._

object ScalaTimeCodecs {



  implicit val encodeLocalDate: Encoder[LocalDate] = Encoder.encodeString.contramap[LocalDate](_.toString)

  implicit val decodeLocalDate: Decoder[LocalDate] = Decoder.decodeString.emap { str =>
    Either.catchNonFatal(LocalDate.parse(str)).leftMap(t => "LocalDate")
  }

  implicit val encodeYear: Encoder[Year] = Encoder.encodeInt.contramap[Year](_.getValue)

  implicit val decodeYear: Decoder[Year] = Decoder.decodeInt.emap { str =>
    Either.catchNonFatal(Year.of(str)).leftMap(t => "Year")
  }
  
  implicit val encodeLocalTime: Encoder[LocalTime] = Encoder.encodeString.contramap[LocalTime](_.toString)

  implicit val decodeLocalTime: Decoder[LocalTime] = Decoder.decodeString.emap { str =>
    Either.catchNonFatal(LocalTime.parse(str)).leftMap(t => "LocalTime")
  }
  
  implicit val encodeDuration: Encoder[Duration] = Encoder.encodeLong.contramap[Duration](_.getSeconds)

  implicit val decodeDuration: Decoder[Duration] = Decoder.decodeLong.emap { str =>
    Either.catchNonFatal(Duration.ofSeconds(str)).leftMap(t => "Duration")
  }


}
package quizleague.domain

import scala.scalajs.js.annotation.JSExportAll

case class ApplicationContext(
  id: String,
  leagueName: String,
  textSet: Ref[GlobalText],
  currentSeason: Ref[Season],
  senderEmail: String,
  emailAliases: List[EmailAlias],
  cloudStoreBucket: String,
  retired: Boolean = false) extends Entity

object ApplicationContext {
  val singletonId = "5659313586569216"
  def apply(
    leagueName: String,
    textSet: Ref[GlobalText],
    currentSeason: Ref[Season],
    senderEmail: String,
    emailAliases: List[EmailAlias],
    cloudStoreBucket: String): ApplicationContext = ApplicationContext(singletonId,
    leagueName,
    textSet,
    currentSeason,
    senderEmail,
    emailAliases,
    cloudStoreBucket)
}

case class EmailAlias(alias: String, user: Ref[User])
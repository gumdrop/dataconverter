package quizleague.domain

case class ApplicationContext (
  id:String,
  leagueName:String,
  textSet:Ref[GlobalText],
  currentSeason:Ref[Season],
  senderEmail:String,
  emailAliases:List[EmailAlias],
  cloudStoreBucket:String,
  retired:Boolean = false  
) extends Entity

case class EmailAlias(alias:String, user:Ref[User])
package quizleague.domain

case class Team (
    id:String,
    name:String,
    shortName:String,
    venue:Ref[Venue],
    text:Ref[Text],
    users:List[Ref[User]] = List(),
    retired:Boolean = false) extends Entity
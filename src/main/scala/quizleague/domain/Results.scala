package quizleague.domain

case class Results(
    id:String, 
    fixtures:Ref[Fixtures],
    results:List[Ref[Result]],
    retired:Boolean = false) extends Entity
    
case class Result(
    id:String, 
    fixture:Ref[Fixture],
    homeScore:Int,
    awayScore:Int,
    submitter:Option[Ref[User]],
    note:String,
    reports:Ref[Reports],
    retired:Boolean = false) extends Entity
    
case class Reports(
    id:String,
    reports:List[Report],
    retired:Boolean = false) extends Entity

case class Report(
    team:Ref[Team],
    text:Ref[Text])
    

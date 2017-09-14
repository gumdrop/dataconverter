package quizleague.domain.container

import quizleague.domain._

case class DomainContainer(
    
    applicationcontext:List[ApplicationContext],
    competition:List[Competition],
    fixtures:List[Fixtures],
    fixture:List[Fixture],
    globaltext:List[GlobalText],
    leaguetable:List[LeagueTable],
    results:List[Results],
    result:List[Result],
    reports:List[Reports],
    season:List[Season],
    team:List[Team],
    text:List[Text],
    user:List[User],
    venue:List[Venue]   

)
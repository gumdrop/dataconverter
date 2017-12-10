package quizleague.domain

import org.threeten.bp.Year

case class Season(
    id:String,
    startYear:Year,
    endYear:Year,
    text:Ref[Text],
    competitions:List[Ref[Competition]],
    calendar:List[CalendarEvent],
    retired:Boolean = false
) extends Entity
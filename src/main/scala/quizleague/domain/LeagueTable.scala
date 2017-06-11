package quizleague.domain

case class LeagueTable(id: String,
  description: String,
  rows: List[LeagueTableRow],
  retired: Boolean = false) extends Entity

case class LeagueTableRow(
  team: Ref[Team],
  position: String,
  played: Int,
  won: Int,
  lost: Int,
  drawn: Int,
  leaguePoints: Int,
  matchPointsFor: Int,
  matchPointsAgainst: Int)
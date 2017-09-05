package quizleague.domain

trait Entity extends Serializable  {
  val id:String
  val retired:Boolean
} 
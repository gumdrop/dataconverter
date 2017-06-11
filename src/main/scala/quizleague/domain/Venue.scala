package quizleague.domain

case class Venue(
    id:String,
    name:String,
    address:String,
    phone:Option[String],
    email:Option[String],
    website:Option[String],
    imageURL:Option[String],
    retired:Boolean = false
) extends Entity
package dataconverter

import dataconverter.mappers.CQLLoader
import dataconverter.mappers.Converter
import io.circe._, io.circe.generic.auto._, io.circe.syntax._
import quizleague.util.json.codecs.DomainCodecs._
import quizleague.util.json.codecs.ScalaTimeCodecs._
import java.io.FileWriter
import java.io.File

object Dataconverter extends App{
  
  val inputFile = args(0)
  
  val input = CQLLoader.load(inputFile)
  
  val output = Converter.convert(input)
  
  val outJson = output.asJson
  
  val writer = new FileWriter(new File("output.json"))
  
  writer.write(outJson.toString)
  
  writer.flush
}
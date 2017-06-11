package dataconverter.mappers

import scala.collection.mutable._
import scala.collection.immutable.{List => IList, Map => IMap}
import scala.reflect.ClassTag
import quizleague.domain.Entity
import org.chilternquizleague.domain.BaseEntity

class ConversionContext(input:IMap[String,IList[BaseEntity]]) {
  
  
  
  val cache = Map[String,ListBuffer[Entity]]()
  
  def add[T <: Entity](o:T)(implicit tag:ClassTag[T]):T = {
    add(List(o))
    o
  }
  def add[T <: Entity](l:List[T])(implicit tag:ClassTag[T]):List[T] = {
    cache += ((tag, cache.getOrElse(tag, ListBuffer()) ++= l))
    l
  }
  
  def getInput[T <: BaseEntity](pred:T=>Boolean)(implicit tag:ClassTag[T]) = input(tag.runtimeClass.getName).asInstanceOf[ListBuffer[T]].filter(pred).headOption
  
  def get[T <: Entity](implicit tag:ClassTag[T]):List[T] = cache(tag).asInstanceOf[ListBuffer[T]].toList

  def get[T <: Entity](id:String)(implicit tag:ClassTag[T]) = cache(tag).filter(_.id == id).head.asInstanceOf[T]
  
  def replace[T <: Entity](in:T)(implicit tag:ClassTag[T]):T = {
    remove(in.id)
    add(in)
    
    in
  }
  
  def remove[T <: Entity](id:String)(implicit tag:ClassTag[T]) = {
    val list = cache(tag)
    val elem = list.find(_.id == id)
    elem.foreach(list -= _)
    list
  }
}
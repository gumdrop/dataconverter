package org.chilternquizleague.util

import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import org.chilternquizleague.domain.BaseEntity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import scala.util.control.Exception.catching
import com.fasterxml.jackson.databind.module.SimpleModule
import org.chilternquizleague.domain.Text
import org.chilternquizleague.domain.User
import com.fasterxml.jackson.databind.ObjectMapper
import org.chilternquizleague.domain._
import java.io.Reader
import scala.reflect.ClassTag
import java.io.Writer
import com.fasterxml.jackson.databind.ObjectMapper

object ClassUtils {
  val packages: List[Package] = List(classOf[BaseEntity].getPackage());

  def classFromPart[T](part: String) = {

    val className = part.substring(0, 1).toUpperCase() + part.substring(1)
    def fun(c: Option[Class[T]], p: Package): Option[Class[T]] = {
      import scala.util.control.Exception._

      if (c.isDefined) c else catching(classOf[NoClassDefFoundError]) opt Class.forName(p.getName() + "." + className).asInstanceOf[Class[T]]
    }
    packages.foldLeft(Option[Class[T]](null))(fun)
  }
}

object JacksonUtils {

  implicit class ObjectMapperImprovements(val o: ObjectMapper) {

    def read[T](reader: Reader)(implicit tag: ClassTag[T]): T = {
      o.readValue(reader, tag.runtimeClass).asInstanceOf[T]
    }
  }
  
  

  class RefDeserializer extends JsonDeserializer[Ref[_]] {
    override def deserialize(parser: JsonParser, context: DeserializationContext): Ref[_] = {
      val node: JsonNode = parser.getCodec().readTree(parser);

      val className = node.get("refClass").asText
      val opt = ClassUtils.classFromPart[BaseEntity](className)

      val remote = for {
        clazz <- opt
      } yield {
        parser.getCodec().treeToValue(node, clazz)
      }
      
     Ref(remote.get.id.toString())
      
 
    }
  }


  lazy val unsafeModule = {
    new SimpleModule()
      .addDeserializer(classOf[Ref[_]], new RefDeserializer())

  }

  lazy val mapper =  new ObjectMapper registerModule unsafeModule

}
package org.chilternquizleague.domain.util

import scala.annotation.meta.field


object JacksonAnnotations {
  import com.fasterxml.jackson.annotation
  
  type JsonIgnore = annotation.JsonIgnore @field
  type JsonProperty = annotation.JsonProperty @field
  type JsonGetter = annotation.JsonGetter @field
  type JsonSetter = annotation.JsonSetter @field
}
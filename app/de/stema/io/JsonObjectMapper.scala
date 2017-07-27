package de.stema.io

import javax.inject.Singleton

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.util.{Failure, Success, Try}

@Singleton
class JsonObjectMapper {
  private lazy val mapper = new ObjectMapper with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.registerModule(new JavaTimeModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.configure(SerializationFeature.INDENT_OUTPUT, true)
  mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
  mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)


  def getJson(obj: Any): String =
    Try {
      mapper.writeValueAsString(obj)
    } match {
      case Success(json) => json
      case Failure(e)    => throw JsonWriteException(s"Error during serialization for '$obj'", e)
    }
}

case class JsonReadException(msg: String, e: Throwable) extends JsonException(msg, e)
case class JsonWriteException(msg: String, e: Throwable) extends JsonException(msg, e)
class JsonException(msg: String, e: Throwable) extends RuntimeException(msg, e)

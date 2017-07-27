package de.stema.io

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.google.inject.Inject
import play.api.Configuration

import scala.reflect.runtime.{universe => ru}
import ru._

class CSVDataMapper @Inject() (configuration: Configuration) {

  def transformDataTo[T: TypeTag](data: List[Map[String, String]]): List[T] = {
    val member = typeOf[T].members
      val allFields = member.
        collect {
          case symbol: TermSymbol =>
            symbol
        }.
        filter(s =>
          s.isVal || s.isVar || (s.isGetter && s.isAccessor && s.isTerm))

    val fieldsWithAnnotation = allFields.flatMap(field =>
      // TODO check if >>> field.annotations.find(_.tree.tpe =:= typeOf[CSVData]). <<< works as expected
      field.annotations.find(_.tpe =:= typeOf[CSVData]).
        map((field, _))).
      toList

    val mirror = ru.runtimeMirror(getClass.getClassLoader)

    data.map { singleData =>
      // for each entry in file, create a new instance of given data-model of type T
      val inst = createInstance(typeOf[T])
      val instanceMirror = mirror.reflect(inst)
      fieldsWithAnnotation.foreach { field =>
        // for each annotated field in class of type T, get the name that is defined within annotation
        val fieldName = getFieldName(field)
        fieldName match {
          case conf if conf == "play-configuration" => setConfiguration(field, configuration, instanceMirror)
          case name =>
            // get the value of the current data-set (one line of csv) by the annotation-name
            singleData.get(name).foreach { fieldValue =>
            // set the value of the annotated field to the value that came from csv
            setFieldValue(field, fieldValue, instanceMirror)
        }
        }
      }
      inst.asInstanceOf[T]
    }
  }

  private def getFieldName(field: (TermSymbol, Annotation)) = {
    val fieldName = field._2.tree.children.tail.head.toString()
    fieldName.stripSuffix("\"").stripPrefix("\"")
  }

  private def setFieldValue(field: (TermSymbol, Annotation), fieldValue: String, instanceMirror: InstanceMirror) = {
    val dateFormat = DateTimeFormatter.ofPattern("dd.MM.yy")

    val fieldMirror = instanceMirror.reflectField(field._1)
    val t = field._1.typeSignature

    if(t =:= typeOf[String])        fieldMirror.set(fieldValue)
    if(t =:= typeOf[Boolean])       fieldMirror.set(fieldValue.toBoolean)
    if(t =:= typeOf[BigDecimal])    fieldMirror.set(BigDecimal.apply(fieldValue.replace(',', '.')))
    if(t =:= typeOf[BigInt])        fieldMirror.set(BigInt.apply(fieldValue))
    if(t =:= typeOf[LocalDate])     fieldMirror.set(LocalDate.parse(fieldValue, dateFormat))
    if(t =:= typeOf[Configuration]) fieldMirror.set(configuration)
  }

  private def setConfiguration(field: (TermSymbol, Annotation), configuration: Configuration, instanceMirror: InstanceMirror) = {
    val fieldMirror = instanceMirror.reflectField(field._1)
    val t = field._1.typeSignature
    if(t =:= typeOf[Configuration]) fieldMirror.set(configuration)
  }

  private def createInstance(tpe:Type): Any = {
    // "standard" way to create an instance out of a Type
    val mirror = ru.runtimeMirror(getClass.getClassLoader)
    val clsSym = tpe.typeSymbol.asClass
    val clsMirror = mirror.reflectClass(clsSym)
    val ctorSym = tpe.decl(ru.termNames.CONSTRUCTOR).asMethod
    val ctorMirror = clsMirror.reflectConstructor(ctorSym)
    val instance = ctorMirror()
    instance
  }
}

package de.stema.io

import scala.reflect.runtime.{universe => ru}
import ru._

class CSVDataMapper {

  def transformDataTo[T: TypeTag](data: List[Map[String, String]]): List[T] = {
    val allFields =
      typeOf[T].
        members.
        collect { case symbol: TermSymbol => symbol }.
        filter(s => s.isVal || s.isVar)

    val annotatedFields = allFields.flatMap(f =>
      f.annotations.find(_.tpe =:= typeOf[CSVData]).
        map((f, _))).
      toList

    val mirror = ru.runtimeMirror(getClass.getClassLoader)

    data.map { singleData =>
      // for each entry in file, create a new instance of given data-model of type T
      val inst = createInstance(typeOf[T])
      annotatedFields.foreach { field =>
        // for each annotated field in class of type T, get the name that is defined within annotation
        /* FIXME fieldNames got by reflection contains leading and trailing quotes like
        ""myName"". therefore we have either ensure, that the data (from csv) contains also quotes
        within name, but that sucks, so create a sulution for that...
        */
        val fieldName = field._2.tree.children.tail.head.toString()
        // get the value of the current data-set (one line of csv) by the annotation-name
        singleData.get(fieldName).foreach { fieldValue =>
          val instanceMirror = mirror.reflect(inst)
          val fieldMirror = instanceMirror.reflectField(field._1)
          // set the value of the annotated field to the value that came from csv
          fieldMirror.set(fieldValue)
        }
      }
      inst.asInstanceOf[T]
    }
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

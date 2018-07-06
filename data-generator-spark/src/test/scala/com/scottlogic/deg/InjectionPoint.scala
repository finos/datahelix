package com.scottlogic.deg

import com.google.inject.{Guice, Module}
import com.google.inject.util.Modules
import org.junit.jupiter.api.extension.{BeforeTestExecutionCallback, ExtensionContext}
import org.junit.jupiter.api.extension.ExtensionContext.Namespace

import scala.collection.JavaConversions

class InjectionPoint extends BeforeTestExecutionCallback {
  @throws[Exception]
  override def beforeTestExecution(context: ExtensionContext): Unit = {
    val modules = List[Module](new TestModule, new SharedModule)
    val test = context.getTestInstance
    if (test.isPresent) {
      val requiresInjection = test.get.getClass.getAnnotation(classOf[RequiresInjection])
      if (requiresInjection != null) {
        import scala.collection.JavaConversions._
        for (c <- requiresInjection.values) {
          modules.add(c.newInstance)
        }
      }
      val aggregate = Modules.combine(JavaConversions.asJavaIterable(modules))
      val injector = Guice.createInjector(aggregate)
      injector.injectMembers(test.get)
      getStore(context).put(injector.getClass, injector)
    }
  }

  private def getStore(context: ExtensionContext) = context.getStore(Namespace.create(getClass))
}
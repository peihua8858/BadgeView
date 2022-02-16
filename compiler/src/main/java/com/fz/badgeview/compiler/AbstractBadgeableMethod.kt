package com.fz.badgeview.compiler

import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic

abstract class AbstractBadgeMethod(protected val packageName:String,protected val processingEnv: ProcessingEnvironment) :
    IBadgeMethod {
    protected var mFileUtils: Filer = processingEnv.filer
    protected var mElementUtils: Elements = processingEnv.elementUtils
    protected var mMessager: Messager = processingEnv.messager

    protected fun isAssignable(childClazz: String, superClazz: String): Boolean {
        return processingEnv.typeUtils.isAssignable(
            mElementUtils.getTypeElement(childClazz).asType(),
            mElementUtils.getTypeElement(superClazz).asType()
        )
    }

    protected fun error(element: Element, message: String, vararg args: Any) {
        var message1: String = message
        if (args.isNotEmpty()) {
            message1 = String.format(message1, *args)
        }
        mMessager.printMessage(Diagnostic.Kind.ERROR, message1, element)
    }

    protected fun checkAnnotationValid(annotatedElement: Element, clazz: Class<*>): Boolean {
        if (annotatedElement.kind != ElementKind.CLASS) {
            error(annotatedElement, "%s must be declared on class.", clazz.simpleName)
            return false
        }
        if (annotatedElement.modifiers.contains(Modifier.PRIVATE)) {
            error(
                annotatedElement,
                "%s must can not be private.",
                (annotatedElement as TypeElement).qualifiedName
            )
            return false
        }
        return true
    }
}
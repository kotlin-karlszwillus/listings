package com.openpromos.jni.nrexpressionlib.value

/**
 * Created by voigt on 30.06.16.
 */
class Element(var type: ElementType, name: String) {

    var name: String? = null

    enum class ElementType {
        Single,
        Multi
    }

    init {
        this.name = name
    }

    override fun toString(): String {
        when (this.type) {
            Element.ElementType.Single -> return "$" + this.name!!
            else -> return "$$" + this.name!!
        }
    }
}

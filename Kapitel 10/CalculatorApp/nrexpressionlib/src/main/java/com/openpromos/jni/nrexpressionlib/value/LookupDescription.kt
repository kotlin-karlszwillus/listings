package com.openpromos.jni.nrexpressionlib.value

import java.util.ArrayList

/**
 * Created by voigt on 30.06.16.
 */
class LookupDescription(elements: ArrayList<Element>) {
    var elements: ArrayList<Element>? = null
        internal set

    init {
        this.elements = elements
    }

    override fun toString(): String {
        var returnString = ""

        for (i in this.elements!!.indices) {
            val element = this.elements!![i]
            returnString = returnString + element.toString()
        }

        return returnString
    }
}

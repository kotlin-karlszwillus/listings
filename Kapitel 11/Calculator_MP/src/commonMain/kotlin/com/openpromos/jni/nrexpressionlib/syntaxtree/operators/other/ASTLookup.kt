package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.other

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Element
import com.openpromos.jni.nrexpressionlib.value.LookupDescription
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.runtime.Runtime

/**
 * Created by voigt on 30.06.16.
 */
class ASTLookup(elements: ArrayList<Element>) : ASTExpression {
    internal var lookup: LookupDescription

    init {
        this.lookup = LookupDescription(elements)
    }

    override fun evaluate(runtime: Runtime): Value {
        try {
            return runtime.lookup(this.lookup)
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString(): String {
        return this.lookup.toString()
    }
}

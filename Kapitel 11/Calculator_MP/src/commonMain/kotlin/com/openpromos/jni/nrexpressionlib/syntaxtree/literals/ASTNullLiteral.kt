package com.openpromos.jni.nrexpressionlib.syntaxtree.literals

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.value.NullValue

/**
 * Representation of Null in the Expression Language
 */
object ASTNullLiteral : ASTLiteral() {

    override fun evaluate(runtime: Runtime) = NullValue()

    override fun toString() = "NULL"
}

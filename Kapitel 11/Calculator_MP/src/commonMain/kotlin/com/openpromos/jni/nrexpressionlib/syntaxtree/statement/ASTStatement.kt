package com.openpromos.jni.nrexpressionlib.syntaxtree.statement

import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTNode
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Basic single statement
 */
open class ASTStatement : ASTNode {

    override fun evaluate(runtime: Runtime): Value {
        return NullValue()
    }
}

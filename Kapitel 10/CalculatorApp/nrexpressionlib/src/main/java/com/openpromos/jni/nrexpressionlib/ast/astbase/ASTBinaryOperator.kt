package com.openpromos.jni.nrexpressionlib.ast.astbase

import com.openpromos.jni.nrexpressionlib.Runtime
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 * Base class for infix operators with two operands.
 */
open class ASTBinaryOperator(var lhs: ASTExpression? = null, var rhs: ASTExpression? = null) : ASTExpression() {

    fun toString(operatorString: String): String {
        return "(" + this.lhs.toString() + " " + operatorString + " " + this.rhs.toString() + ")"
    }
}

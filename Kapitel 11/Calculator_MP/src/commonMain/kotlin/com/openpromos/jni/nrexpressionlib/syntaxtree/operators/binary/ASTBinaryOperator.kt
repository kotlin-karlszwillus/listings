package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression

/**
 * Created by voigt on 22.06.16.
 *
 * Base class for infix operators with two operands.
 */
abstract class ASTBinaryOperator(var lhs: ASTExpression? = null, var rhs: ASTExpression? = null) : ASTExpression {

    fun toString(operatorString: String) = "(${this.lhs.toString()} $operatorString ${this.rhs.toString()})"
}

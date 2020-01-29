package com.openpromos.jni.nrexpressionlib.syntaxtree.operators.binary

import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.BoolValue
import com.openpromos.jni.nrexpressionlib.value.Value
import com.openpromos.jni.nrexpressionlib.runtime.Runtime

/**
 * Created by voigt on 29.06.16.
 */
class ASTEqual : ASTBinaryOperator {
    constructor() : super() {}

    constructor(lhs: ASTExpression, rhs: ASTExpression) : super(lhs, rhs) {}

    override fun evaluate(runtime: Runtime): Value {

        try {
            val lhsVal = this.lhs!!.evaluate(runtime)
            val rhsVal = this.rhs!!.evaluate(runtime)

            return BoolValue(lhsVal.isEqualTo(rhsVal))
        } catch (e: Exception) {
            throw e
        }

    }

    override fun toString() = super.toString("==")
}

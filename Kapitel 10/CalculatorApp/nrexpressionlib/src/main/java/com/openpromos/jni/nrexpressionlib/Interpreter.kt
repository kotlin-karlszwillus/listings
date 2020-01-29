package com.openpromos.jni.nrexpressionlib

import android.util.Log

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Simple implementation of the interpreter to demo the first
 * working expression - this will be subject to change as the implementation
 * moves on (as of 18th of July)
 */
object Interpreter {

    fun evaluateExpression(source: String, delegate: RuntimeDelegate, uuid: String): Value? {

        val parser = Parser(Lexer(source))
        val runtime = Runtime(delegate)

        try {
            //Log.d("Interpreter", source);
            delegate.setUuid(uuid)
            val node = parser.parseExpression()
            val returnValue = node.evaluate(runtime)
            delegate.resetUuid()
            return returnValue
        } catch (e: Exception) {
            //Log.e("Interpreter", "Caught an interpreter exception", e);
            return null
        }

    }
}

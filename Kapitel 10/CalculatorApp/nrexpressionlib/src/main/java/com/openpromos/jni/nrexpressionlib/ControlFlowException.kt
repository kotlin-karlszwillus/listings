package com.openpromos.jni.nrexpressionlib

/**
 * Created by voigt on 05.07.16.
 */
class ControlFlowException(reason: String, var exceptionType: ControlFlow) : Exception(reason) {
    enum class ControlFlow {
        RETURN,
        CONTINUE,
        BREAK
    }
}

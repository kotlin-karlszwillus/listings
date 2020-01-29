package com.openpromos.jni.nrexpressionlib.parser

/**
 * Created by voigt on 11.07.16.
 */
class ParserException(errorType: ParserErrorType) : Exception(errorType.toString()) {

    private var errorType = ParserErrorType.Unspecified

    enum class ParserErrorType {
        InvalidToken,
        ValueOutOfRange,
        UnexpectedToken,
        UnexpectedEnd,
        Unspecified
    }

    init {
        this.errorType = errorType
    }

    override fun toString(): String {
        return this.errorType.toString()
    }
}

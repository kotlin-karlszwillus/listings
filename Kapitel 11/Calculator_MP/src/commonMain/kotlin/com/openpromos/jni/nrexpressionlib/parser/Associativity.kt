package com.openpromos.jni.nrexpressionlib.parser

/**
 * The only right associative operators are unary not and unary minus.
 * They are not parsed in _parsePrimaryExpression, so they are not listed here.
 */
enum class Associativity {
    Left,
    Right
}

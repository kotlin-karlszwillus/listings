package com.openpromos.jni.nrexpressionlib.parser

/**
 * Created by voigt on 28.06.16.
 */
enum class Precedence private constructor(val value: Int) {
    Lowest(0),
    Exceptional(1),
    Conditional(2),
    Functional(3),
    LogicalOr(4),
    LogicalAnd(5),
    Contains(6),
    Equality(7),
    Relational(8),
    Additive(9),
    Multiplicative(10),
    Prefix(11),
    Member(12)
}

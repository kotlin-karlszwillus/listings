package com.openpromos.jni.nrexpressionlib.value

/**
 * Created by voigt on 22.06.16.
 *
 * The `Value` type implements all built-in or bridged types and their values.
 */
enum class ValueType {
    Null,
    Bool,
    Number,
    Date,
    String,
    List,
    Dictionary,
    Callable,
    Object
}

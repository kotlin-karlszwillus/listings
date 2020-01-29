package com.openpromos.jni.nrexpressionlib.value

/**
 * Created by voigt on 30.06.16.
 *
 * To use an application defined object as a value, it must conform to the `Bridgeable`
 * interface.
 */
interface Bridgeable {
    fun nrx_typeString(): String
    fun nrx_isEqual(other: Bridgeable): Boolean
    fun nrx_debugDescription(): String
    fun nrx_callable(): nrx_Callable
}

package com.openpromos.jni.nrexpressionlib

import com.openpromos.jni.nrexpressionlib.value.LookupDescription
import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 22.06.16.
 */
interface RuntimeDelegate {
    fun resolve(symbol: String): Value?
    fun lookup(lookupDescription: LookupDescription): Value
    fun print(string: String)

    fun setUuid(uuid: String)
    fun resetUuid()
}

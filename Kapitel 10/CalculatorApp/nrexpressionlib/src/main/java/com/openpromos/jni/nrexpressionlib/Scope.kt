package com.openpromos.jni.nrexpressionlib

import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

import java.util.Hashtable

/**
 * Created by voigt on 05.07.16.
 */
class Scope {
    var symbols: Hashtable<String, Value>

    constructor() {
        this.symbols = Hashtable()
    }

    constructor(other: Scope?) {
        if (other != null) {
            this.symbols = other.symbols
        } else {
            this.symbols = Hashtable()
        }
    }

    fun getSymbol(symbol: String): Value? {
        return this.symbols[symbol]
    }

    fun setSymbol(symbol: String, newValue: Value) {
        this.symbols[symbol] = newValue
    }

    fun count(): Int {
        return this.symbols.size
    }
}

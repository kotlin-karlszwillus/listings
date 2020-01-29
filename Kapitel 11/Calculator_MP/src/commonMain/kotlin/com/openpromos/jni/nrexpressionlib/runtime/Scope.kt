package com.openpromos.jni.nrexpressionlib.runtime

import com.openpromos.jni.nrexpressionlib.value.Value

/**
 * Created by voigt on 05.07.16.
 */
class Scope {
    var symbols: HashMap<String, Value>

    constructor() {
        this.symbols = HashMap()
    }

    constructor(other: Scope?) {
        if (other != null) {
            this.symbols = other.symbols
        } else {
            this.symbols = HashMap()
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

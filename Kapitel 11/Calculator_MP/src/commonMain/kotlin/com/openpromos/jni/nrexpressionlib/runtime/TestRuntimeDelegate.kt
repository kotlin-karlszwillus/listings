package com.openpromos.jni.nrexpressionlib.runtime

import com.openpromos.jni.nrexpressionlib.value.*

/**
 * Created by voigt on 04.07.16.
 */
class TestRuntimeDelegate : RuntimeDelegate {
    internal var output: ArrayList<String>
    internal var symbols: HashMap<String, Value>

    init {
        this.output = ArrayList()
        this.symbols = HashMap()
        this.symbols["testVariable"] = StringValue.withValue("testVariable's value")
        this.symbols["testFunction"] = CallableValue(TestNrxCallable())
    }

    override fun resolve(symbol: String): Value? {
        return this.symbols[symbol]
    }

    override fun lookup(lookupDescription: LookupDescription): Value {
        return StringValue.withValue(lookupDescription.toString())
    }

    override fun print(string: String) {
        this.output.add(string)
    }

    override fun toString(): String {
        var returnString = ""

        for (string in this.output) {
            returnString = returnString + string + "\n"
        }

        return returnString
    }

    override fun setUuid(uuid: String) {
        // Do nothing until we test lookup
    }

    override fun resetUuid() {
        // Do nothing until we test lookup
    }
}

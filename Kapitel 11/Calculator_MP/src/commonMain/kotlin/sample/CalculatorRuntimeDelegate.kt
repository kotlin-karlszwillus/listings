package sample

import com.openpromos.jni.nrexpressionlib.runtime.RuntimeDelegate
import com.openpromos.jni.nrexpressionlib.value.LookupDescription
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value

class CalculatorRuntimeDelegate : RuntimeDelegate {

    val uuidStack = mutableListOf<String>()

    override fun lookup(lookupDescription: LookupDescription): Value {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun print(string: String) {
        println(string)
    }

    override fun setUuid(uuid: String) {
        uuidStack.plus(uuid)
    }

    override fun resetUuid() : Unit {
        if (!uuidStack.isEmpty()) uuidStack.removeAt(uuidStack.size-1)
    }

    override fun resolve(symbol: String): Value? {
        return NullValue()
    }

}

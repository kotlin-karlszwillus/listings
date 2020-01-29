package kmb.com.kotlinsnippets

import com.openpromos.jni.nrexpressionlib.RuntimeDelegate
import com.openpromos.jni.nrexpressionlib.value.LookupDescription
import com.openpromos.jni.nrexpressionlib.value.NullValue
import com.openpromos.jni.nrexpressionlib.value.Value
import java.util.*

class CalculatorRuntimeDelegate : RuntimeDelegate {

    val uuidStack = Stack<String>()

    override fun lookup(lookupDescription: LookupDescription): Value {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun print(string: String) {
        println(string)
    }

    override fun setUuid(uuid: String) {
        uuidStack.push(uuid)
    }

    override fun resetUuid() : Unit {
        if (!uuidStack.empty()) uuidStack.pop()
    }

    override fun resolve(symbol: String): Value? {
        return NullValue()
    }

}

package kmb.com.kotlinsnippets

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.openpromos.jni.nrexpressionlib.Interpreter
import kotlinx.android.synthetic.main.activity_main.result
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.input
import kmb.com.kotlinsnippets.extensions.randomString
import java.math.BigDecimal
import java.math.BigInteger

class MainActivity : AppCompatActivity() {

    val runtime = CalculatorRuntimeDelegate()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            if (it is Button) result.text = interpretEquation(input.text.toString())
        }
    }

    fun interpretEquation(input: String) : String {
        return Interpreter.evaluateExpression(input, runtime, "start").toString()
    }

    fun doMath(a : Int, b : Int) : Long {
        return a.toLong() * b
    }
}


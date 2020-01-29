import com.openpromos.jni.nrexpressionlib.EvaluationError
import com.openpromos.jni.nrexpressionlib.lexer.Lexer
import com.openpromos.jni.nrexpressionlib.lexer.TokenType
import com.openpromos.jni.nrexpressionlib.parser.Parser
import com.openpromos.jni.nrexpressionlib.runtime.Runtime
import com.openpromos.jni.nrexpressionlib.runtime.RuntimeDelegate
import com.openpromos.jni.nrexpressionlib.syntaxtree.ASTExpression
import com.openpromos.jni.nrexpressionlib.value.*
import kotlin.test.*
/**
 * Created by voigt on 06.07.16.
 */
private fun optimizedBuild(): Boolean {
    return true
}

private fun sourceString(): String {
    var source = "0\n"
    val numLoops = if (optimizedBuild()) 1000 else 1

    for (i in 1..numLoops) {
        source += "\t+ (([\"0\", \"2\", \"$i\", \$foo, [], \"Hello, World!\", \"\\\"\", \"1️⃣\"]"
        source += " map element : (NUMBER(element) except 0)) where each: each % 2 == 1).count"
    }

    return source
}
@ExperimentalStdlibApi
class PerformanceTests {

    private fun sourceBuffer(): ByteArray {
        val sourceString = sourceString()

        val stringBytes = sourceString.encodeToByteArray()
        return stringBytes
    }

    private fun testLexerPerformance() {
        val numLoops = if (optimizedBuild()) 500 else 1

        // val millis = System.currentTimeMillis()
        var lexer: Lexer? = null

        for (i in 0 until numLoops) {
            try {
                lexer = Lexer(sourceBuffer())
            } catch (e: Exception) {
                println(e.toString())
            }

            if (lexer != null)
                tokenLoop@ while (true) {
                    when (lexer.scanToken()!!.type) {
                        TokenType.AtEnd -> break@tokenLoop
                        TokenType.LexerError -> fail("unexpected lexer error")
                        else -> continue@tokenLoop
                    }
                }
        }

        //val duration = System.currentTimeMillis() - millis
       // println("LexerPerformanceTest needed: $duration milliseconds")
    }

    private fun testParserPerformance() {
        val numLoops = if (optimizedBuild()) 50 else 1

        //val millis = System.currentTimeMillis()
        var lexer: Lexer? = null

        for (i in 0 until numLoops) {
            try {
                lexer = Lexer(sourceBuffer())
            } catch (e: Exception) {
                println(e.toString())
            }

            val parser = Parser(lexer!!)
            var node: ASTExpression? = null
            try {
                node = parser.parseExpression()
            } catch (e: Exception) {
                println(e.toString())
            }

            assertNotNull(node)
            assertTrue(parser.isAtEnd)
        }

       // val duration = System.currentTimeMillis() - millis
       // println("ParserPerformanceTest needed: $duration milliseconds")
    }

    private fun testRuntimePerformance() {
        var lexer: Lexer? = null

        try {
            lexer = Lexer(sourceBuffer())
        } catch (e: Exception) {
            println(e.toString())
        }

        val parser = Parser(lexer!!)
        var node: ASTExpression? = null

        try {
            node = parser.parseExpression()
        } catch (e: Exception) {
            println(e.toString())
        }

        if (node == null) {
            fail("parsing failed unexpectedly")
        }

        assertTrue(parser.isAtEnd)

        val delegate = TestDelegate()
        val runtime = Runtime(delegate)

        val numLoops = if (optimizedBuild()) 5 else 1

        //val millis = System.currentTimeMillis()
        for (i in 0 until numLoops) {
            var result: Value? = null
            try {
                result = node!!.evaluate(runtime)
            } catch (e: Exception) {
                println(e.toString())
            }

            if (optimizedBuild()) {
                assertEquals(result!!.toString(), "500")
            } else {
                assertEquals(result!!.toString(), "1")
            }
        }

       // val duration = System.currentTimeMillis() - millis
       // println("RuntimerPerformanceTest needed: $duration milliseconds")
    }

    inner class TestDelegate : RuntimeDelegate {

        override fun resolve(symbol: String): Value? {
            return if (symbol == "NUMBER") {
                CallableValue(NUMBER())
            } else null
        }

        override fun lookup(lookupDescription: LookupDescription): Value {
            return StringValue.withValue("foo")
        }

        override fun print(string: String) {}

        override fun setUuid(uuid: String) {

        }

        override fun resetUuid() {

        }

        override fun toString(): String {
            return ""
        }

        inner class NUMBER : nrx_Callable {
            override fun getParameterNames(): ArrayList<String> {
                val returnList = ArrayList<String>()
                returnList.add("value")
                return returnList
            }

            override fun body(runtime: Runtime): Value {
                var value: Value? = null
                try {
                    value = runtime.resolve("value")
                } catch (e: Exception) {
                    throw e
                }

                when (value) {
                    is NumberValue -> return value
                    is StringValue -> {
                        val number = (value.stringValue().toDouble())
                        return NumberValue(number)
                    }
                    else -> {
                    }
                }
                return NullValue()
                //throw new EvaluationError("could not convert to number");
            }
        }
    }

    @Test
    fun TestLexerPerformance() {
        testLexerPerformance()
    }

    @Test
    fun TestParserPerformamce() {
        testParserPerformance()
    }

    @Test
    fun TestRuntimePerformance() {
        testRuntimePerformance()
    }
}


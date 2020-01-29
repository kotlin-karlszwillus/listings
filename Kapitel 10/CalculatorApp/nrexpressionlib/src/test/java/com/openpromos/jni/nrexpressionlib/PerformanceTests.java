package com.openpromos.jni.nrexpressionlib;

import com.openpromos.jni.nrexpressionlib.ast.astbase.ASTExpression;
import com.openpromos.jni.nrexpressionlib.value.CallableValue;
import com.openpromos.jni.nrexpressionlib.value.LookupDescription;
import com.openpromos.jni.nrexpressionlib.value.NumberValue;
import com.openpromos.jni.nrexpressionlib.value.StringValue;
import com.openpromos.jni.nrexpressionlib.value.Value;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * Created by voigt on 06.07.16.
 */
public class PerformanceTests {
    private static boolean optimizedBuild() {
        return true;
    }

    private static String sourceString() {
        String source = "0\n";
        int numLoops = PerformanceTests.optimizedBuild() ? 1000 : 1;

        for (int i = 1; i <= numLoops; i++) {
            source += "\t+ (([\"0\", \"2\", \"" + i + "\", $foo, [], \"Hello, World!\", \"\\\"\", \"1️⃣\"]";
            source += " map element : (NUMBER(element) except 0)) where each: each % 2 == 1).count";
        }

        return source;
    }

    private static byte[] sourceBuffer() throws UnsupportedEncodingException {
        String sourceString = PerformanceTests.sourceString();

        byte[] stringBytes=sourceString.getBytes("UTF-8");
        byte[] ntBytes=new byte[stringBytes.length+1];
        System.arraycopy(stringBytes, 0, ntBytes, 0, stringBytes.length);

        return ntBytes;
    }

    private void testLexerPerformance() {
        int numLoops = PerformanceTests.optimizedBuild() ? 500 : 1;

        long millis = System.currentTimeMillis();

        Lexer lexer = null;

        for (int i = 0; i < numLoops; i++) {
            try {
                lexer = new Lexer(PerformanceTests.sourceBuffer());
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            if (lexer != null)
                tokenLoop: while (true) {
                    switch (lexer.scanToken().getType()) {
                        case AtEnd:
                            break tokenLoop;
                        case LexerError:
                            fail("unexpected lexer error");
                            break;
                        default:
                            continue;
                    }
                }
        }

        long duration = System.currentTimeMillis() - millis;
        System.out.println("LexerPerformanceTest needed: " + duration + " milliseconds");
    }

    private void  testParserPerformance() {
        int numLoops = PerformanceTests.optimizedBuild() ? 50 : 1;

        long millis = System.currentTimeMillis();
        Lexer lexer = null;

        for (int i = 0; i < numLoops; i++) {
            try {
                lexer = new Lexer(PerformanceTests.sourceBuffer());
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            Parser parser = new Parser(lexer);
            ASTExpression node = null;
            try {
                node = parser.parseExpression();
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            Assert.assertNotNull(node);
            Assert.assertTrue(parser.isAtEnd());
        }

        long duration = System.currentTimeMillis() - millis;
        System.out.println("ParserPerformanceTest needed: " + duration + " milliseconds");
    }

    private void testRuntimePerformance() {
        Lexer lexer = null;

        try {
            lexer = new Lexer(PerformanceTests.sourceBuffer());
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        Parser parser = new Parser(lexer);
        ASTExpression node = null;

        try {
            node = parser.parseExpression();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        if (node == null) {
            fail("parsing failed unexpectedly");
        }

        Assert.assertTrue(parser.isAtEnd());

        TestDelegate delegate = new TestDelegate();
        Runtime runtime = new Runtime(delegate);

        int numLoops = PerformanceTests.optimizedBuild() ? 5 : 1;

        long millis = System.currentTimeMillis();
        for (int i = 0; i < numLoops; i++) {
            Value result = null;
            try {
                result = node.evaluate(runtime);
            } catch (Exception e) {
                System.out.println(e.toString());
            }

            if (PerformanceTests.optimizedBuild()) {
                assertThat(result.toString(), is(equalTo("500")));
            } else {
                assertThat(result.toString(), is(equalTo("1")));
            }
        }

        long duration = System.currentTimeMillis() - millis;
        System.out.println("RuntimerPerformanceTest needed: " + duration + " milliseconds");
    }

    public class TestDelegate implements RuntimeDelegate {
        public TestDelegate() {
        }

        public Value resolve(String symbol) {
            if (symbol.equals("NUMBER")) {
                return new CallableValue(new NUMBER());
            }
            return null;
        }

        public Value lookup(LookupDescription lookupDescription) {
            return StringValue.Companion.withValue("foo");
        }

        public void print(String string) {
        }

        @Override
        public void setUuid(String uuid) {
            
        }

        @Override
        public void resetUuid() {

        }

        public String toString() {
            String returnString = "";
            return returnString;
        }

        public class NUMBER implements nrx_Callable {
            public ArrayList<String> getParameterNames()
            {
                ArrayList<String> returnList = new ArrayList<>();
                returnList.add("value");
                return returnList;
            }
            public Value body(Runtime runtime) throws Exception {
                Value value = null;
                try {
                    value = runtime.resolve("value");
                } catch (Exception e) {
                    throw e;
                }

                switch (value.getValueType()) {
                    case Number:
                        return value;
                    case String: {
                        double number = Double.parseDouble(value.stringValue());
                        return new NumberValue(number);
                    }
                    default:
                        break;
                }

                throw new EvaluationError("could not convert to number");
            }
        }
    }

    @Test
    public void TestLexerPerformance() {
        testLexerPerformance();
    }

    @Test
    public void TestParserPerformamce() {
        testParserPerformance();
    }

    @Test
    public void TestRuntimePerformance() {
        testRuntimePerformance();
    }
}


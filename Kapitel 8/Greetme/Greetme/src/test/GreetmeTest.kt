import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.SystemOutRule

class TestGreetme {
    @get:Rule
    val systemOutput : SystemOutRule = SystemOutRule().enableLog()

    @Test fun testOk() {
        // Call main method to start our program
        main(emptyArray())

        // Capture system log
        val logOutput = systemOutput.log
        val lineSeparator = System.lineSeparator()

        // Make the comparison against the expected result
        Assert.assertEquals("Hello world!$lineSeparator", logOutput)
    }
}

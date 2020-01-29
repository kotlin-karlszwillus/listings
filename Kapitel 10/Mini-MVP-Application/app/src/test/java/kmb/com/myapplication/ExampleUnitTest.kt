package kmb.com.myapplication

import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Mock
    lateinit var view: Contracts.View

    @Test
    fun presenterWorks() {
        MockitoAnnotations.initMocks(this)
        val presenter = Presenter(view)
        assertEquals(1, presenter.greetCount)
        presenter.greetButtonClicked()
        verify(view).updateGreeting(1)
    }
}

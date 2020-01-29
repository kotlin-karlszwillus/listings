package kmb.com.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), Contracts.View {

    val presenter = Presenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buttonGreet.setOnClickListener {
            presenter.greetButtonClicked()
        }
    }

    override fun updateGreeting(count: Int) {
        textGreet.text = getString(R.string.hello_world_num, count)
    }
}

package kmb.com.myapplication

interface Contracts {

    interface View {
       fun updateGreeting(count: Int)
    }

    interface Presenter {
        fun greetButtonClicked()
    }
}
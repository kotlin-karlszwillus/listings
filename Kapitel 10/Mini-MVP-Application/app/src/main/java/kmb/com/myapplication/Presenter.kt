package kmb.com.myapplication

class Presenter(val view: Contracts.View): Contracts.Presenter {

    var greetCount = 1

    override fun greetButtonClicked() {
        view.updateGreeting(greetCount++)
    }
}
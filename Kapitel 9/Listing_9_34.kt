class TheAnswer {
   val value: Int by lazy {
       println("Calculation done")
       42
   }
}

fun main(args: Array<Strings>) {
	val theAnswer = TheAnswer()
	println(theAnswer.value)
	println("Hello, world!")
	println(theAnswer.value)
}

fun square(i : Int) = i * i

fun main (args : Array<String>) {
	if (args.size > 0) {
		for (argument in args) {
			val input = argument.toIntOrNull()
			input?.let { println(square(it)) } ?: println("Ganze Zahlen bitte")
		}
	}
	else {
		println("Bitte mindestens eine Zahl als Parameter angeben")
	}
}
fun square(i : Int) = i * i

fun main (args : Array<String>) {
	if (args.isNotEmpty()) {
 		val input = args[0].toIntOrNull()
 		input?.let { println(square(input)) } ?: println("Dit war aba keene Zahl")
	}
 	else {
 		println("Bitte eine Zahl als Parameter angeben")
 	}
 }
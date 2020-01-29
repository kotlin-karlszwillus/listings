fun main (args : Array<String>) {
	val greeting = if (args.size > 0) args[0] else "Enter greeting"
	
	// For mit in Operator für Arrays
	for (c in greeting) println (c)
	
	// For mit Index
	for (n in 0..(greeting.length - 1)) {
		println(greeting[n])
	}
}
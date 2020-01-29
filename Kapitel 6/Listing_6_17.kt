inline fun doMath(i : Int, mathFunction: (p : Int) -> Number) = mathFunction(i)
	
fun main(args : Array<String>) {
	val number : Int = args.getOrNull(0)?.toInt() ?: 1
    for (i in 1..number) {
    	println(doMath(number ?: 0, {it * it}))
    }
}

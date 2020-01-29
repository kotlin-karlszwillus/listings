import kotlin.math.*

fun doMath(i : Int?, function: (p : Int) -> Number) : Number {
    val secNumber = i ?: 0
    return function(secNumber)
}

fun main(args : Array<String>) {
	val number : Int? = args.getOrNull(0)?.toInt();
	println(doMath(number, {it * it}))
    println(doMath(number, { sqrt(it.toFloat())}))
    val faculty: (Int) -> Int = { var result = 1 
                            for (num in 1..it) { 
                            	result *= num 
                            }
                            result }
    println(doMath(number, faculty))
}

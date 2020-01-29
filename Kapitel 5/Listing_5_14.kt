import kotlin.math.*

fun siebForMe(maxNum: Int = 1000): Array<Boolean> {
	var seave: Array<Boolean> = Array(maxNum +1) {_-> false}
	
    for (i in 2..sqrt(maxNum.toDouble()).roundToInt()) {
 		if (!seave[i]) {
 			// Remove multiples of i*i
			for (j in i*i..maxNum step i) {
				seave[j] = true
			}
		}
	}
	return seave
}

fun main(args: Array<String>) {
	 for ((index,isPrime) in siebForMe().withIndex()) {
     	if (isPrime) println(index)
     }
}

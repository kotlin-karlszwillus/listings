import kotlin.math.sqrt

fun main (args : Array<String>) {
	val number = 2 * 2
	
	val format = "DIN A$number"
	val amount_due = "$number\$"
	val gedicht = """Eins zu Wurzel zwei
	Normal (${sqrt(2f)})
	
	Verlacht haben sie dich, $format"""
	
	println(gedicht)
	println(format)
	println(amount_due)
}
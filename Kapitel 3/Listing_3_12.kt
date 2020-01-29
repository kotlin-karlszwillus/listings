data class Car(val make: String, val model: String, var price: Int? = null) {
	fun getMarketingInfo() = "$make $model"
	fun isForSale() = price != null
}
       
fun main(args: Array<String>) {
	val cars = mutableListOf(Car("Ford", "Model T", 7000),
							 Car("Volkswagen", "T3", 10000))
	
	if (args.size > 1) {
		val price = if (args.size > 2) args[2].toIntOrNull() else null
		cars.add(Car(args[0], args[1], price))
	}
	else {
		println("Add your own car by adding using cars <make> <model> <your price>")
		println("Leave the price empty if you are not willing to sell")
	}
	
	val mostExpensive = cars.filter { it.isForSale() } .maxBy {  it.price ?: Int.MAX_VALUE }
	println("The most expensive is ${mostExpensive?.getMarketingInfo()}")
 }
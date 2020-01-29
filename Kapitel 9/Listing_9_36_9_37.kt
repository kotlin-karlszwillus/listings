import kotlin.properties.Delegates

class AuctionBidding(val startingPrice: Int = 1) {

    var price by Delegates.vetoable(startingPrice) {
		 _, oldVal, newVal ->
			if (newVal is Int && (oldVal == null || newVal > oldVal)) {
 				onBidChanged.takeIf { ::onBidChanged.isInitialized }?.invoke(newVal)
				true // Rückgabewert des Lambdas
 			} else false // Rückgabewert des Lambdas
 	}

 	lateinit var onBidChanged: ((Int) -> Unit)
}

fun main(args: Array<String>) {
	val bidding = AuctionBidding(2)
    bidding.onBidChanged = { price -> println("$price zum Ersten...") }
 	
    bidding.price = 5
 	bidding.price = 10
 	bidding.price = 7
 	bidding.price = 0
 	bidding.price = 18
}
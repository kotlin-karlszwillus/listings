fun main(args : Array<String>) {
	val ar : Array<Array<Int>> = arrayOf(arrayOf(1,2,3), arrayOf(2,3,5))
	val size = 3
	val ar2 : Array<Array<Int>> = Array(size, { outer -> Array(size, { inner -> outer * size + inner })})

	for (i in 0 until size) {
   		for (j in 0 until size)
       		print("${ar2[i][j]}, ")
   		println()
	}	
}

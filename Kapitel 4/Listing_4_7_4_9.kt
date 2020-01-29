fun main(args: Array<String>) {
    var i : Int? = 5;
	val j : Int = i ?: 0;
	i = 10;
	println("$i $j");
    
    val namesList : Array<String> = arrayOf("Karl", "Jakob")
    val secondNames = namesList.copyOf();
    
    for (name in namesList) {
        println(name)
    }
	
    namesList.set(0, "Hannah")
	
    for (name in namesList) {
        println(name)
    }
    println("Second list:")
    println("============")
    
    for (name in secondNames) {
        println(name)
    }
}

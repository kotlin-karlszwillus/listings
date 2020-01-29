fun main(args: Array<String>) {
    var names = arrayOf("Karl", "Rolf")
	var stringNames = arrayOf("Horst", "Kevin", "Magnus", "Kurt")
	var anyDates = listOf("Karl", 3, arrayOf(1, 2, 3))
    
    var namesMap = mapOf(Pair("Szwillus", listOf("Karl")), Pair("Meier", listOf("David", "Patrick")))
	
    var listNames = listOf("Karl", "Sücru", "Feline")
	    
	names = stringNames
    anyDates = listNames
    
    println(namesMap["Szwillus"])
    namesMap["Szwillus"]
    namesMap.get("Szwillus")
    
    names.filter {it.startsWith("K")}. forEach {
        println(it)
    }
    
    for ((key, value) in names.groupBy { it.get(0) }.iterator())
    	println("$key => $value")

    
    names.get(0)
    names.elementAt(0)
    names[0]
    
    listNames.get(0)
    listNames.elementAt(0)
    listNames[0]
}

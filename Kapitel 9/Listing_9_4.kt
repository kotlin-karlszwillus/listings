class ComplexName {
    companion object {
        const val NO_SURNAME = -1
   }

   var firstName = "Konstantin"
   var lastName = "Serhan"

   val surnames = listOf("Meier", "Müller")

   fun calculateLength(name: String = this.lastName) =
	   name?.let { if (it in surnames) it.length else NO_SURNAME } 
}
	
fun main(args: Array<String>) {
	println(ComplexName().calculateLength("Meier"))
}
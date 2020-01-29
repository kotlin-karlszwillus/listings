package sample

import org.w3c.dom.*
import kotlin.browser.document
import kotlin.dom.createElement

data class Car(val make: String, val model: String, val price: Int? = null)

class CarExample {

    fun HTMLDivElement.printResult(cars: List<Car>, row: (Car)-> HTMLDivElement) {
        cars.forEach { this.appendChild(row(it)) }
    }

    fun analyze() {
        val value = document.getElementById("cars") as HTMLTextAreaElement
        val myCars: Array<Car> = JSON.parse(value.value)
        (document.getElementById("result") as HTMLDivElement).printResult(myCars.asList(),
            {
                    car -> document.createElement("div") {
                            this.innerHTML = "${car.make} ${car.model} "
                            this.className = "forsale".takeIf { car.price != null } ?: "regular"
                    } as HTMLDivElement
            })
    }

    fun manipulateHTML() {
        val bodyElement = document.body
        bodyElement?.children?.asList()?.forEach {
            when (it) {
                is HTMLDivElement -> it.innerHTML = "div" + it.innerHTML
                is HTMLHRElement -> it.size = "8"
            }
        }
    }

    fun manipulateHTMLSelector() {
        val allDivs = document.getElementsByTagName("div")
        allDivs.asList().forEach {
            it.innerHTML = "div" + it.innerHTML
        }
    }

    fun writeValue(s: String) {
        (document.getElementById("result") as HTMLDivElement).innerHTML = s
    }
}
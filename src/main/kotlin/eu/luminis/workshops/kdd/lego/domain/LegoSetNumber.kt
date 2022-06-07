package eu.luminis.workshops.kdd.lego.domain

// https://www.lego.com/en-us/service/help/identifying-lego-set-and-part-numbers-ka000
data class LegoSetNumber(val number: Int) {
    init {
        require(number in 100..9999999) { "Lego set number must have 3 to 7 digits" }
    }

    override fun toString() = number.toString()
}
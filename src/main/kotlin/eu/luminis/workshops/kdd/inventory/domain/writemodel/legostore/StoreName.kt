package eu.luminis.workshops.kdd.inventory.domain.writemodel.legostore

data class StoreName(val name: String) {
    init {
        if (name.isBlank()) throw IllegalArgumentException("Store name cannot be blank")
        if (name.trim() != name) throw IllegalArgumentException("Store name cannot start or end with whitespace")
    }
}
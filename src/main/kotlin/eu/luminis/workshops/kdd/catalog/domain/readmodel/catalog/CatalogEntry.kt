package eu.luminis.workshops.kdd.catalog.domain.readmodel.catalog

import eu.luminis.workshops.kdd.lego.domain.LegoSetNumber
import java.nio.file.Path

data class CatalogEntry(
    val id: LegoSetNumber,
    val productTitle: String,
    val imageUri: Path,
    val popular: Boolean = false,
    val assumedAmountInStock: Int = 0,
)

fun fromFilename(filename: String): CatalogEntry {
    val (nr, title) = """(\d+)-(.+)\.jpg""".toRegex().find(filename)!!.destructured
    return CatalogEntry(
        LegoSetNumber(nr.toInt()),
        title.capitalizeWords(),
        Path.of(filename),
        popular = filename.contains("lego-popular/")
    )
}

fun fromPath(path: Path): CatalogEntry {
    return fromFilename(path.toString()).copy(
        imageUri = path
    )
}

@Suppress("DEPRECATION")
private fun String.capitalizeWords(): String = replace("-", " ")
    .split(" ").map { it.capitalize() }.joinToString(" ")